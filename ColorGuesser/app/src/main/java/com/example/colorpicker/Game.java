package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Game extends AppCompatActivity {

    boolean[] filled = new boolean[]{false, false, false};
    static ArrayList<Integer> rs;
    static ArrayList<Integer> gs;
    static ArrayList<Integer> bs;
    static ArrayList<Double> ds;
    double avg;
    int red;
    int green;
    int blue;
    ArrayList<EditText> colors = new ArrayList<>();
    EditText re;
    EditText ge;
    EditText be;
    TextView rt;
    TextView gt;
    TextView bt;
    TextView actualtv;
    TextView rv;
    TextView gv;
    TextView bv;
    TextView resultt;
    TextView distt;
    Button submit;
    Button menu;
    Button next;
    ConstraintLayout bg;
    ImageView thumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bg = findViewById(R.id.mylayout);

        re = findViewById(R.id.rg);
        ge = findViewById(R.id.gg);
        be = findViewById(R.id.bg);
        colors.add(re);
        colors.add(ge);
        colors.add(be);

        for (final EditText e : colors) {
            e.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (e.getText().toString().contains("-")) {
                        String[] str = e.getText().toString().split("-");
                        if (str.length > 1) {
                            e.setText(e.getText().toString().split("-")[1]);
                        } else {
                            e.setText("");
                        }
                    }
                    if (!e.getText().toString().isEmpty() || e.getText().toString().length() > 0) {
                        filled[colors.indexOf(e)] = true;
                        if (Integer.parseInt(e.getText().toString()) > 255) {
                            e.setText("255");
                        }
                    }

                    if (e.getText().toString().isEmpty()) filled[colors.indexOf(e)] = false;
                    if (filled[0] && filled[1] && filled[2]) {
                        submit.setEnabled(true);
                    } else {
                        submit.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) { }
            });
        }

        rv = findViewById(R.id.rgt);
        gv = findViewById(R.id.ggt);
        bv = findViewById(R.id.bgt);
        rt = findViewById(R.id.ra);
        gt = findViewById(R.id.ga);
        bt = findViewById(R.id.ba);
        menu = findViewById(R.id.menu);
        next = findViewById(R.id.next);
        menu = findViewById(R.id.menu);
        submit = findViewById(R.id.submit);
        submit.setEnabled(false);
        actualtv = findViewById(R.id.actt);
        resultt = findViewById(R.id.goodworse);
        distt = findViewById(R.id.distanceTxtView);
        thumb = findViewById(R.id.imageView);
        red = (int) (Math.random() * 256);
        green = (int) (Math.random() * 256);
        blue = (int) (Math.random() * 256);
        bg.setBackgroundColor(Color.rgb(red, green, blue));


        update();

        if (rs == null) {
            rs = new ArrayList<>();
            gs = new ArrayList<>();
            bs = new ArrayList<>();
            ds = new ArrayList<>();
        }

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.setSelection(re.getText().length());
            }
        });

    }

    public void submit(View v) {
        System.out.println(re.getText().toString());

        rt.setText(red + "");
        gt.setText(green + "");
        bt.setText(blue + "");
        rv.setText("Red Guess");
        gv.setText("Green Guess");
        bv.setText("Blue Guess");

        actualtv.setVisibility(View.VISIBLE);
        rt.setVisibility(View.VISIBLE);
        gt.setVisibility(View.VISIBLE);
        bt.setVisibility(View.VISIBLE);
        menu.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        submit.setVisibility(View.INVISIBLE);

        int r = red - Integer.parseInt(re.getText().toString());
        int g = green - Integer.parseInt(ge.getText().toString());
        int b = blue - Integer.parseInt(be.getText().toString());
        double dist = Math.sqrt((Math.pow(r, 2.0) + Math.pow(g, 2.0) + Math.pow(b, 2.0)));
        dist = Math.round(dist * 100.0) / 100.0;
        avg = 0;
        for (int i = 0; i < ds.size(); i++) {
            avg += ds.get(i);
        }
        avg = avg / ds.size();

        if (rs.size() > 0) {
            if (dist < avg) {
                thumb.setImageResource(R.drawable.up);
                resultt.setText("Better than your average");
            } else {
                thumb.setImageResource(R.drawable.down);
                resultt.setText("Worse than your average!");
            }
            thumb.setVisibility(View.VISIBLE);
        } else {
            resultt.setText("You have no previous scores.");
        }
        distt.setText("Your guess was a distance of " + dist + " off.");
        resultt.setVisibility(View.VISIBLE);
        distt.setVisibility(View.VISIBLE);

        rs.add(red);
        gs.add(green);
        bs.add(blue);
        ds.add(dist);
    }

    public void next(View v) {
        actualtv.setVisibility(View.INVISIBLE);
        rt.setVisibility(View.INVISIBLE);
        gt.setVisibility(View.INVISIBLE);
        bt.setVisibility(View.INVISIBLE);
        menu.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        resultt.setVisibility(View.INVISIBLE);
        distt.setVisibility(View.INVISIBLE);
        thumb.setVisibility(View.INVISIBLE);

        rv.setText("Red Guess");
        gv.setText("Green Guess");
        bv.setText("Blue Guess");

        for (EditText e : colors) {
            e.setText("");
        }
        red = (int) (Math.random() * 256);
        green = (int) (Math.random() * 256);
        blue = (int) (Math.random() * 256);
        submit.setVisibility(View.VISIBLE);
        bg.setBackgroundColor(Color.rgb(red, green, blue));
    }

    public void menu(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    public static ArrayList<Integer> getrs() {
        return rs;
    }

    public static ArrayList<Integer> getgs() {
        return gs;
    }

    public static ArrayList<Integer> getbss() {
        return bs;
    }

    public static ArrayList<Double> getds() {
        return ds;
    }

    public void update() {

        try {
            rs = Scores.getrs();
            gs = Scores.getrgs();
            bs = Scores.getbs();
            ds = Scores.getds();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
