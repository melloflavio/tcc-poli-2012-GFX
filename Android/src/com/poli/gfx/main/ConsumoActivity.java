package com.poli.gfx.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.Plot;
import com.androidplot.xy.XYStepMode;
import com.poli.gfx.R;
import com.poli.gfx.model.Residencia;
import com.poli.gfx.util.SharedPreferencesAdapter;

public class ConsumoActivity extends Activity{	
	
	private static final String TAG = ConsumoActivity.class.getSimpleName();
	
//	private ArrayList<String> mHouseNames;
	private ArrayList<Boolean> mDrawGraphStates;
	private ListView mListView;
	private ArrayList<Integer> mColors = new ArrayList<Integer>();
	private ArrayList<LineAndPointFormatter> _formatters = new ArrayList<LineAndPointFormatter>(); 
	private ArrayList<Residencia> _residencias;
	private ArrayList<XYSeries> _series = new ArrayList<XYSeries>();
	
	private XYPlot _mainXYPlot;
	
	//Activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);
        
        ((TextView) findViewById(R.id.header_title)).setText("Medidas");
        
//        mHouseNames = new ArrayList<String>();
        _residencias = geraDadosResidência();
        
        mDrawGraphStates = new ArrayList<Boolean>();
        mDrawGraphStates.add(Boolean.valueOf(true));
        mDrawGraphStates.add(Boolean.valueOf(true));
        
//        getHousesFromSharedPrefs();
        setColors();
        
        mListView = (ListView) findViewById(R.id.consumo_listView);
        mListView.setAdapter(new ListAdapter());
        
        geraSeriesConsumoDia(new Date(2012, 11, 20));
        
        _mainXYPlot = (XYPlot) findViewById(R.id.consumo_mySimpleXYPlot);
        
        drawGraph();
    }
    
    @Override
   	protected void onDestroy() {
   		if (isFinishing()) {
   			   		   	      
   		}
   		
   		super.onDestroy();
   	}
    
    private void getHousesFromSharedPrefs(){
    	int houseCount = SharedPreferencesAdapter.getIntFromSharedPreferences(SharedPreferencesAdapter.HOUSE_COUNT_KEY, getApplicationContext());
        
    	String houseName;
    	String houseId;
    	
        for (int i = 0 ; i < houseCount ; i++){
        	houseName = SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_NAME_KEY+i, getApplicationContext());
        	houseId= SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+i, getApplicationContext());
        	
        	if(houseName != null && houseId != null){
        		Log.d(TAG, "house found! id = "+houseId+" name = "+houseName);
//        		mHouseIds.add(houseId);
//        		mHouseNames.add(houseName);
        		mDrawGraphStates.add(Boolean.valueOf(true));
        	}
        }
    }
    
   /* private ArrayAdapter<String> getSimpleAdapter(final Context context, ArrayList<String> list){
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout., list){
    		public View getView(int position, View convertView, ViewGroup parent) {
    			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			View rowView = inflater.inflate(R.layout.consumo_list_item, parent, false);
    			
    			((TextView)rowView.findViewById(R.id.consumo_list_nomeCasa)).setText(mHouseNames.get(position));
    			((TextView)rowView.findViewById(R.id.consumo_list_color)).setBackgroundColor(mColors.get(position).intValue());
    			
	             return rowView;
	     }
    	
    	};
		
		
		
		return adapter;
    }*/
    
    
    private void setColors(){
    	int c;
    	
    	c = getResources().getColor(R.color.blue_dark);
    	mColors.add(Integer.valueOf(c));
    	_formatters.add(new LineAndPointFormatter(c, c, null));
    	
    	c = getResources().getColor(R.color.yellow);
    	mColors.add(Integer.valueOf(c));
    	_formatters.add(new LineAndPointFormatter(c, c, null));
    	
    	
    }
    
    private class ListAdapter extends BaseAdapter{

		public int getCount() {
			return _residencias.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.consumo_list_item, parent, false);
			}
			
			
			((TextView)convertView.findViewById(R.id.consumo_list_nomeCasa)).setText(_residencias.get(position).getNomeCasa());
			((TextView)convertView.findViewById(R.id.consumo_list_color)).setBackgroundColor(mColors.get(position).intValue());
			((CheckBox)convertView.findViewById(R.id.consumo_list_checkBox)).setChecked(true);
			
			convertView.setClickable(true);
			
			convertView.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					Log.d(TAG, "AW YEAH!!!"+position);
//					((CheckBox)v.findViewById(R.id.consumo_list_checkBox)).setChecked(true);
					CheckBox c = ((CheckBox)v.findViewById(R.id.consumo_list_checkBox));
					c.toggle();
					mDrawGraphStates.set(position, Boolean.valueOf(c.isChecked()));
					drawGraph();
				}
			});
			
            return convertView;
		}
    	
    }
    
    private void drawGraph(){
    	
    	_mainXYPlot.clear();   	
        
    	drawGraphDia();
    	
        _mainXYPlot.redraw();
        Log.d(TAG, "Top Max2 = "+_mainXYPlot.getRangeTopMax());
        
        _mainXYPlot.disableAllMarkup();
        Log.d(TAG, "Top Max3 = "+_mainXYPlot.getRangeTopMax());
    }
    
    private void drawGraphDia(){
    	
    	_mainXYPlot.getGraphWidget().setMarginTop(2);
    	_mainXYPlot.getGraphWidget().setMarginRight(2);
    	_mainXYPlot.setPlotPadding(10, 10, 10, 10);
    	_mainXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
    	
    	_mainXYPlot.setTitle("Consumo ao longo do dia");
    	
        for (int i = 0 ; i < _series.size() ; i++){
        	if(mDrawGraphStates.get(i)){
        		_mainXYPlot.addSeries(_series.get(i), _formatters.get(i)); 
        	}
        }
        
    	//sets range formatting
    	_mainXYPlot.setRangeStep(XYStepMode.SUBDIVIDE, 5);
    	_mainXYPlot.setRangeValueFormat(new DecimalFormat("#"));
    	_mainXYPlot.setRangeLabel("Potência consumida (kWh)");
    	Log.d(TAG, "Top Max = "+_mainXYPlot.getRangeTopMax());
//    	_mainXYPlot.setRangeUpperBoundary(new Integer(_mainXYPlot.getRangeTopMax().intValue()+1), BoundaryMode.FIXED);
    	
    	
    	//sets domain formatting
    	_mainXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
    	_mainXYPlot.setTicksPerDomainLabel(2);
    	_mainXYPlot.setDomainValueFormat(new DecimalFormat("#"));
    	_mainXYPlot.setDomainUpperBoundary(24, BoundaryMode.FIXED);
    	_mainXYPlot.setDomainLabel("Hora do Dia");
    }
    
    private ArrayList<Residencia> geraDadosResidência(){
    	ArrayList<Residencia> residencias = new ArrayList<Residencia>();
    	Residencia r = new Residencia();
    	r.setNomeCasa("Casa 1");
    	Date horaInicio;
    	Date horaTermino;
    	for (int i = 0 ; i < 48 ; i++){
    		horaInicio = new Date(2012, 11, 20, i/2, (i*30)%60);
    		horaTermino = new Date(2012, 11, 20, i/2, 15 + (i*30)%60);
    		r.adicionarMedida(i*3.0, horaInicio, horaTermino); 
    	}
    	residencias.add(r);
    	
    	r = new Residencia();
    	r.setNomeCasa("Casa 2");
    	for (int i = 0 ; i < 20 ; i++){
    		horaInicio = new Date(2012, 11, 20, i/2, (i*30)%60);
    		horaTermino = new Date(2012, 11, 20, i/2, 15 + (i*30)%60);
    		r.adicionarMedida(30.0 - i, horaInicio, horaTermino); 
    	}
    	residencias.add(r);
    	return residencias;
    }
    
    private void geraSeriesConsumoDia(Date data){
    	_series = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		_series.add(r.geraDadosGráficoConsumoData(data));
    	}
    }
    
}
