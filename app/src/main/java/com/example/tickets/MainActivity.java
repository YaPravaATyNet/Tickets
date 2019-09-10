package com.example.tickets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    HashSet<Segment>[][] array;
    String number;
    int result;
    boolean power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void check(View view) {
        if (!fillFields()) {
            Toast.makeText(getApplicationContext(), "Не все поля заполнены", Toast.LENGTH_SHORT).show();
            return;
        }
        HashSet<Segment> res = getSet();
        TextView textView = (TextView) findViewById(R.id.answer);
        for (Segment curSegment : res) {
            if (curSegment.getValue() == result) {
                textView.setText("Yes");
                return;
            }
        }
        textView.setText("No");
    }

    public void answer(View view) {
        if (!fillFields()) {
            Toast.makeText(getApplicationContext(), "Не все поля заполнены", Toast.LENGTH_SHORT).show();
            return;
        }
        HashSet<Segment> res = getSet();
        TextView textView = (TextView) findViewById(R.id.answer);
        String answer = "";
        for (Segment curSegment : res) {
            if (curSegment.getValue() == result) {
                answer = toString(curSegment, 0, number.length() - 1);
                break;
            }
        }
        textView.setText(answer);
    }

    public String toString(Segment segment, int begin, int end) {
        //System.err.println(segment.getSign());
        if ((segment.getSign() == 'a') || (segment.getSign() == '-')) {
            return String.valueOf((int)segment.getValue());
        } else {
            String first = makeBrackets(segment.getFirstValue(), segment.getSign(), begin, segment.getSeparator());
            String second = makeBrackets(segment.getSecondValue(), segment.getSign(), segment.getSeparator() + 1, end);
            String res = first + String.valueOf(segment.getSign()) + second;
            if ((segment.getSign() == '+') && (second.charAt(0) == '-')) {
                res = first + second;
            }
            return res;
        }
    }

    public String makeBrackets(double value, char sign, int begin, int end) {
        HashSet<Segment> cur = array[begin][end];
        String part = "";
        for (Segment curSegment : cur) {
            if (value == curSegment.getValue()) {
                part = toString(curSegment, begin, end);
                if (((sign == '*') || (sign == '/') || (sign == '^')) && ((curSegment.getSign() == '-') || (curSegment.getSign() == '+'))) {
                    return  "(" + part + ")";
                }
                if ((sign == '^') && (curSegment.getSign() != 'a')) {
                    return  "(" + part + ")";
                }
                if ((sign == '/') && (curSegment.getSign() == '/')) {
                    return  "(" + part + ")";
                }
            }
        }
        return part;
    }

    public boolean fillFields() {
        EditText etNumber = (EditText)findViewById(R.id.number);
        EditText etResult = (EditText)findViewById(R.id.result);
        if (etNumber.getText().toString().length() == 0 || etResult.getText().toString().length() == 0) {
            return false;
        }
        number = etNumber.getText().toString();
        result = Integer.parseInt(etResult.getText().toString());
        array = new HashSet[number.length()][number.length()];
        for (int i = 0; i < number.length(); i++) {
            for (int j = 0; j < number.length(); j++) {
                array[i][j] = new HashSet<>();
            }
        }
        RadioButton rb = (RadioButton) findViewById(R.id.power_yes);
        if (rb.isChecked()) {
            power = true;
        } else {
            power = false;
        }
        return true;
    }

    public HashSet<Segment> getSet(){
        for (int i = 0; i < number.length(); i++) {
            array[i][i].add(new Segment(Double.parseDouble(number.substring(i, (i + 1))), i, 'a')); //это можно засунуть вниз
            array[i][i].add(new Segment(-Double.parseDouble(number.substring(i, (i + 1))), i, '-'));
        }
        for (int len = 1; len < number.length(); len++) {
            for (int begin = 0; begin < number.length() - len; begin++) {
                for (int sep = begin; sep < begin + len; sep++) {
                    cartesian(array[begin][begin + len], array[begin][sep], array[sep + 1][begin + len], sep);
                }
                array[begin][begin + len].add(new Segment(Double.parseDouble(number.substring(begin, begin + len + 1)), begin, 'a'));
                array[begin][begin + len].add(new Segment(-Double.parseDouble(number.substring(begin, begin + len + 1)), begin, 'a'));
            }
        }
        return array[0][number.length() - 1];
    }

    public void cartesian(HashSet<Segment> res, HashSet<Segment> first, HashSet<Segment> second, int sep) {
        for (Segment firstSegment : first) {
            for (Segment secondSegment : second) {
                getSegments(res, firstSegment.getValue(), secondSegment.getValue(), sep);
            }
        }
    }

    public void getSegments(HashSet<Segment> res, double a, double b, int sep) {
        res.add(new Segment(a + b, a, b, sep, '+'));
        res.add(new Segment(a * b, a, b, sep, '*'));
        res.add(new Segment(a / b, a, b, sep, '/'));
        if (power) {
            res.add(new Segment(Math.pow(a, b), a, b, sep, '^'));
        }
    }

}
