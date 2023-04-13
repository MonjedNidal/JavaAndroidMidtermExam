package com.monjed.maswadeh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    MyAdapter myAdapter;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Student> students = new ArrayList<>(Arrays.asList(
                new Student("Alaa", "Nablus", "0569657252"),
                new Student("Sami", "Ramallah", "0569657252"),
                new Student("Monjed", "Hebron", "0569657252"),
                new Student("Ali", "Jerusalem", "0569657252"),
                new Student("Bilal", "Jafa", "0569657252")
        ));

        myAdapter = new MyAdapter(students,this);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(myAdapter);

        EditText searchName, searchAddress, searchPhone;
        searchName = findViewById(R.id.searchName);
        searchAddress = findViewById(R.id.searchAddress);
        searchPhone = findViewById(R.id.searchPhone);
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = searchName.getText().toString();
                String address = searchAddress.getText().toString();
                String phone = searchPhone.getText().toString();
                if(!(name.equals("") || address.equals("") || phone.equals(""))){
                    Student temp = new Student(name,address,phone);
                    students.add(0,temp);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

    }

        class MyAdapter extends BaseAdapter implements Filterable {
            ArrayList<Student> allStudents;
            ArrayList<Student> filteredStudents;
            Context context;

            public MyAdapter(ArrayList<Student> students, Context context) {
                allStudents = students;
                filteredStudents = students;
                this.context = context;
            }

            @Override
            public int getCount() {
                return filteredStudents.size();
            }

            @Override
            public Object getItem(int i) {
                return filteredStudents.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                @SuppressLint("ViewHolder")
                View view = getLayoutInflater().inflate(R.layout.list_item,parent, false );

                TextView name = view.findViewById(R.id.name);
                TextView address = view.findViewById(R.id.address);
                TextView phoneNum = view.findViewById(R.id.phoneNum);

                name.setText(filteredStudents.get(position).name);
                address.setText(filteredStudents.get(position).address);
                phoneNum.setText(filteredStudents.get(position).phoneNum);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToDetails(position);
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        PopupMenu popup = new PopupMenu(context, view);
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @SuppressLint("NonConstantResourceId")
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()){
                                    case R.id.dial:
                                        dialNumber(filteredStudents.get(position).phoneNum);
                                        return true;
                                    case R.id.nextPage:
                                        goToDetails(position);
                                        return true;
                                    case R.id.delete:
                                        deleteItem(filteredStudents,position);
                                        return true;
                                    default:return false;
                                }
                            }
                        });

                        popup.show();
                        return true;
                    }
                });
                return view;
            }

            private void goToDetails(int position){
                Intent intent = new Intent(MainActivity.this,Details.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", filteredStudents.get(position).name);
                bundle.putString("address", filteredStudents.get(position).address);
                bundle.putString("phoneNum", filteredStudents.get(position).phoneNum);

                intent.putExtras(bundle);
                startActivity(intent);
            }
            private void dialNumber(String phoneNumber) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
            private void deleteItem(ArrayList<Student>filteredStudents,int position) {
                filteredStudents.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public Filter getFilter() {
                Filter filter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults filterResults = new FilterResults();

                        if (constraint == null || constraint.length() == 0){
                            filterResults.values = allStudents;
                            filterResults.count = allStudents.size();
                        }else {
                            String s = constraint.toString().toLowerCase();
                            ArrayList<Student> result = new ArrayList<>();
                            for (Student student: allStudents) {
                                if(student.name.toLowerCase().contains(s)){
                                    result.add(student);
                                }
                            }
                            filterResults.values = result;
                            filterResults.count = result.size();
                        }

                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                        filteredStudents = (ArrayList<Student>) filterResults.values;
                        notifyDataSetChanged();
                    }
                };
                return filter;
            }
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}