package com.meshsami27.android_phpmysql.ui.ui.Update;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meshsami27.android_phpmysql.R;
import com.meshsami27.android_phpmysql.ui.model.Note;
import com.meshsami27.android_phpmysql.ui.ui.main.MainActivity;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    EditText et_title, et_note;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    private Context context;
    private int color, id;
    private List <Note> notes;
    private SpectrumPalette mSpectrumPalette;
    private int selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        et_title = findViewById(R.id.title);
        et_note = findViewById(R.id.note);
        palette = findViewById(R.id.palette);

        mSpectrumPalette = findViewById(R.id.palette);

        mSpectrumPalette.setHorizontalScrollBarEnabled(true);
        mSpectrumPalette.setFixedColumnCount(17);


        mSpectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(@ColorInt int color) {
                selectedColor = color;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:

                final String title = et_title.getText().toString().trim();
                final String note = et_note.getText().toString().trim();
                final int color = selectedColor;

                if (title.isEmpty()) {
                    et_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    et_note.setError("Please enter a note");
                } else {

                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://my-noter.000webhostapp.com/update.php", new Response.Listener <String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Successfully Updated!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map <String, String> getParams() {
                            Map <String, String> params = new HashMap <>();
                            params.put("title", title);
                            params.put("note", note);
                            params.put("color", String.valueOf(color));
                            System.out.println("&^&&&&&&&" + title);
                            System.out.println("###############" + note);
                            System.out.println("&&&&&&&&&&&&" + color);
                            return params;
                        }
                    };
                    final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                }
                return true;

            //Delete
            case R.id.delete:

                final StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://my-noter.000webhostapp.com/delete.php", new Response.Listener <String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Successfully Deleted!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map <String, String> getParams() {
                        Map <String, String> params = new HashMap <>();
                        params.put("id", String.valueOf(id));
                        return params;
                    }
                };
                final RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                requestQueue2.add(stringRequest2);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm !");
                alertDialog.setMessage("Are you sure?" );
                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestQueue2.stop();
                    }
                });
                alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



