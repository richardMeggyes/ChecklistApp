package com.readdeo.tescochecklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = findViewById(R.id.mainLayout);

        final String[] checkList = {
                // Eladótér
                "Eladótéri hűtők",
                "Csemege / frissáru pultok, pult mögötti eszközök",
                "Áruvédelmi kapuk (pénztári, bejárati)",
                "grill (ahol van)",
                "savanyúság(ahol van)",

                // Előkészítő területek
                "csemege / frissáru előkészítők",
                "Pékség, cukrászat",
                "grill (ahol van)",
                "savanyúság(ahol van)",

                //raktár

                // Egyéb kiszolgáló terület
                "dolgozói büfé/konyha",
                "főpénztár",
                "öltözők",
                "CCTV",
                "Tűzoltórendszer(ek)",
                "Hangrendszer",

                // Tető gépészet
                "légtechnika",
                "keresk. Hűtés",

                // Gépészet
                "kazánház",
                "0,4kV",
                "dízel generátor",
                "UPS",
                "Sprinkler",
                "Transzformátor",
                "20kV főkapcsoló (ha van)",

                // Raktárterület
                "hűtőkamrák (ajtó, kamra, elpárologtató, légfüggöny)",
                "rakodókapuk, gyorskapuk",
                "targoncák, békák, targoncatöltők",
                "rámpa kiegyenlítő",

                // Udvar
                "Teherkapu",
                "Tüzivíz tároló",
                "Rámpa",
                "Dízel generátor",
                "Nagy szemét tömörítő",

                // Bejárat, Mall
                "Biztonsági rács",
                "Légkondi belső egység",
                "Egyéb, nemtom"
        };
        String[] headers = {
                "Eladótér",
                "Előkészítő területek",

                "Egyéb kiszolgáló terület",
                "Tető gépészet",
                "Gépészet",
                "Raktárterület",
                "Udvar",
                "Bejárat, Mall"
        };

        // Kategóriákon belüli darabszám
        int[] headerItems = {
                0, //
                5, // Eladótér
                4, // Előkészítő területek

                6, // Egyéb kiszolgáló terület
                2, // Tető gépészet
                7, // Gépészet
                4, // Raktárterület
                5, // Udvar
                3, // Bejárat, Mall
        };

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        int loopCounter = 0;
        int headerCounter = 0;
        int headerItem = 1;

        // Adding first header to view
        mainLayout.addView(createHeader(headers, 0));

        for (final String listItem : checkList) {

            //Dinamically adding the headers to the view
            if (headerCounter == headerItems[headerItem]) {

                mainLayout.addView(createHeader(headers, headerItem));

                headerItem++;
                headerCounter = 0;
            }

            final int[] state = {0}; // 0 = NOT YET, 1= WORK IN PROGRESS, 2 = FINISHED

            final String prefname = "item_" + String.valueOf(loopCounter);
            if (sharedPref.contains(prefname)) {
                state[0] = sharedPref.getInt(prefname, state[0]);
            } else {
                setPref(prefname, state[0]);
            }

            //Main row
            final LinearLayout row = new LinearLayout(this);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(rowParams);

            //Item TextView
            final TextView txv = new TextView(this);
            txv.setText(listItem);
            txv.setPadding(100, 0, 25, 0);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 55);
            row.setLayoutParams(rowParams);

            int btnSidePadding = 7;

            // FINISHED button
            Button btnFinished = new Button(this);
            btnFinished.setLayoutParams(btnParams);
            btnFinished.setText("Kész");
            btnFinished.setPadding(btnSidePadding,0,btnSidePadding,0);

            btnFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFinishedColors(txv, row);
                    setPref(prefname, 2);
                    state[0] = sharedPref.getInt(prefname, state[0]);
                    updateTxv(listItem, txv);
                    updateInfo(checkList);
                }
            });


            //WORK IN PROGRESS button
            Button btnWIP = new Button(this);
            btnWIP.setLayoutParams(btnParams);
            btnWIP.setText("Folyamatban");
            btnWIP.setPadding(btnSidePadding,0,btnSidePadding,0);

            btnWIP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUnderWorkColors(txv, row);
                    setPref(prefname, 1);
                    state[0] = sharedPref.getInt(prefname, state[0]);
                    updateTxv(listItem, txv);
                    updateInfo(checkList);
                }
            });


            //NOT YET BUTTON
            Button btnNotYet = new Button(this);
            btnNotYet.setLayoutParams(btnParams);
            btnNotYet.setText("Később");
            btnNotYet.setPadding(btnSidePadding,0,btnSidePadding,0);

            btnNotYet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotYetColors(txv, row);
                    setPref(prefname, 0);
                    state[0] = sharedPref.getInt(prefname, state[0]);
                    updateTxv(listItem, txv);
                    updateInfo(checkList);
                }
            });

            //Reset BUTTON
            Button btnreset = findViewById(R.id.btnReset);

            btnreset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetData(checkList);
                    updateTxv(listItem, txv);
                    updateInfo(checkList);
                }
            });

            // View space = new View(parent_context);
            View space = new View(this);

            // Width:0dp, Height:1 & Weight: 1.0
            LinearLayout.LayoutParams spaceLP = new LinearLayout.LayoutParams(0, 1, 1.0f);
            space.setLayoutParams(spaceLP);

            //Set state from sharedprefs
            System.out.println("STATE:" + String.valueOf(state[0]));
            if (state[0] == 0) {
                setNotYetColors(txv, row);
            } else if (state[0] == 1) {
                setUnderWorkColors(txv, row);
            } else if (state[0] == 2) {
                setFinishedColors(txv, row);
            }

            // EditText
//            EditText etName = new EditText(this);
//            etName.setId(2);

            //Adding items to row
            row.addView(txv);
            row.addView(space);

//            row.addView(etName);

            row.addView(btnFinished);
            row.addView(btnWIP);
            row.addView(btnNotYet);

            mainLayout.addView(row);

            loopCounter++;
            headerCounter++;
        }
        updateInfo(checkList);
    }

    public void setFinishedColors(TextView txv, LinearLayout lin) {
        lin.setBackgroundColor(Color.parseColor("#859900"));
        txv.setTextColor(Color.parseColor("#002b36"));
    }

    public void setUnderWorkColors(TextView txv, LinearLayout lin) {
        lin.setBackgroundColor(Color.parseColor("#b58900"));
        txv.setTextColor(Color.parseColor("#002b36"));
    }

    public void setNotYetColors(TextView txv, LinearLayout lin) {
        lin.setBackgroundColor(Color.parseColor("#073642"));
        txv.setTextColor(Color.parseColor("#93a1a1"));
    }

    public boolean intArrayContainsInt(int i, int[] arr) {

        for (int num : arr) {
            if (i == num) {
                return true;
            }
        }
        return false;
    }

    public void setPref(String prefName, int value) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(prefName, value);
        editor.apply();
    }

    public void updateTxv(String text, TextView txv) {
        // TODO Nem kell
        txv.setText(text);
    }

    public void updateInfo(String[] checkList) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        TextView txv = findViewById(R.id.txvInfo);
//        txv.setTextSize(20);

        TextView txv1 = findViewById(R.id.txvInfo1);
//        txv1.setTextSize(20);
        txv1.setTextColor(Color.parseColor("#859900"));

        TextView txv2 = findViewById(R.id.txvInfo2);
//        txv2.setTextSize(20);
        txv2.setTextColor(Color.parseColor("#b58900"));

        TextView txv3 = findViewById(R.id.txvInfo3);
//        txv3.setTextSize(20);

        int listSize = checkList.length;

        int finishedCounter = 0;
        int WIPCounter = 0;
        int notYetCounter = 0;

        for (int x = 0; x < listSize; x++) {
            int itemState = sharedPref.getInt("item_" + String.valueOf(x), 0);
            if (itemState == 2) {
                finishedCounter++;
            } else if (itemState == 1) {
                WIPCounter++;
            } else if (itemState == 0) {
                notYetCounter++;
            }
        }

        String infoText = String.format("Kész/Folyamatban/Később ");
        txv.setText(infoText);
        txv1.setText(String.valueOf(finishedCounter) + "/");
        txv2.setText(String.valueOf(WIPCounter) + "/");
        txv3.setText(String.valueOf(notYetCounter));
    }

    public void resetData(String[] checkList) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int listSize = checkList.length;

        for (int x = 0; x < listSize; x++) {
            setPref("item_" + String.valueOf(x), 0);
            ;
        }

    }

    public LinearLayout createHeader(String[] headers, int i) {
        TextView header0 = new TextView(this);
        header0.setText(headers[i]);
        header0.setTextColor(Color.parseColor("#002b36"));
//        header0.setTextSize(20);
        header0.setPadding(10, 0, 0, 0);

        final LinearLayout headerRow = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerRow.setLayoutParams(rowParams);
        headerRow.addView(header0);
        headerRow.setBackgroundColor(Color.parseColor("#93a1a1"));

        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.parseColor("#93a1a1")); //white background
        border.setStroke(3, 0xFF000000); //black border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            headerRow.setBackgroundDrawable(border);
        } else {
            headerRow.setBackground(border);
        }

        return headerRow;
    }
}
