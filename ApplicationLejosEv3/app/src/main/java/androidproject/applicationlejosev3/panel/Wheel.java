package androidproject.applicationlejosev3.panel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidproject.applicationlejosev3.R;

public class Wheel extends Fragment {

    Button buttonA ;
    Button buttonR ;
    Button buttonG ;
    Button buttonD ;
    Button buttonFast;
    Button buttonSlow ;
    Button buttonExit ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_wheel,container,false);

        buttonA = (Button) v.findViewById(R.id.buttonA);
        buttonR = (Button) v.findViewById(R.id.buttonR);
        buttonG = (Button) v.findViewById(R.id.buttonG);
        buttonD = (Button) v.findViewById(R.id.buttonD);
        buttonFast = (Button) v.findViewById(R.id.buttonAcc);
        buttonSlow = (Button) v.findViewById(R.id.buttonRal);
        buttonExit = (Button) v.findViewById(R.id.buttonQuit);

        return v ;
    }

    public Button getButtonAvancer()
    {
        return buttonA;
    }

    public Button getButtonReculer()
    {
        return buttonR;
    }

    public Button getButtonGauche()
    {
        return buttonG;
    }
    public Button getButtonDroite()
    {
        return buttonD;
    }
    public Button getButtonAccelerer()
    {
        return buttonFast;
    }
    public Button getButtonRalentir()
    {
        return buttonSlow;
    }

    public Button getButtonQuitter()
    {
        return buttonExit;
    }





}
