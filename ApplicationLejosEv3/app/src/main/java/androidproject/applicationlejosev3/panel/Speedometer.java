package androidproject.applicationlejosev3.panel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import androidproject.applicationlejosev3.R;
import de.nitri.gauge.Gauge;

public class Speedometer extends Fragment {

    Gauge gauge ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_speedometer,container,false);
        gauge = v.findViewById(R.id.gauge);
        return v ;
    }

    public void changeValue(int value)
    {
        gauge.setValue(value);
    }

}


