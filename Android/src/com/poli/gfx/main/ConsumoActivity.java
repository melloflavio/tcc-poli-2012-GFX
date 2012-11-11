package com.poli.gfx.main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.poli.gfx.widget.MaxTodayDatePickerDialog;

public class ConsumoActivity extends Activity{	
	
	private static final String TAG = ConsumoActivity.class.getSimpleName();
	
	private static final int INDICE_ANO = 0;
	private static final int INDICE_MES = 1;
	private static final int INDICE_DIA = 2;
	
//	private ArrayList<String> mHouseNames;
	private ArrayList<Boolean> mDrawGraphStates;
	private ListView mListView;
	private ArrayList<Integer> mColors = new ArrayList<Integer>();
	private ArrayList<LineAndPointFormatter> _formatters = new ArrayList<LineAndPointFormatter>(); 
	private ArrayList<Residencia> _residencias;
	private ArrayList<XYSeries> _series = new ArrayList<XYSeries>();
	
	private XYPlot _mainXYPlot;
	
	private Spinner _spinnerTipo;
	
	private final String[] tiposDeGrafico = {"Anual", "Mensal", "Diário"};
	
	private Calendar _datePicked;
	
	//Activity lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo);
        
        ((TextView) findViewById(R.id.header_title)).setText("Medidas");
        
        inicializaVariaveis();
        
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tiposDeGrafico);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinnerTipo.setAdapter(dataAdapter);
        _spinnerTipo.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
				drawGraph();
				setDateLabel(position);
//				findViewById(R.id.consumo_data_edittext).performClick();
				updateDateText();
				geraDataSeries();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
		});
        
//        mHouseNames = new ArrayList<String>();
        
        
        
        
//        getHousesFromSharedPrefs();
        setColors();
        
        geraSeriesConsumoDia();
        
        
        
        mListView = (ListView) findViewById(R.id.consumo_listView);
        mListView.setAdapter(new ListAdapter());
        
        drawGraph();
    }
    
    @Override
   	protected void onDestroy() {
   		if (isFinishing()) {
   			   		   	      
   		}
   		
   		super.onDestroy();
   	}
    
    
    
    private void inicializaVariaveis(){
    	_datePicked = Calendar.getInstance();
        
        _spinnerTipo = (Spinner) findViewById(R.id.consumo_spinner_tipo);
        
        
        //Testing
        //TODO
        _residencias = geraDadosResidência();
        
        
        //TODO
        mDrawGraphStates = new ArrayList<Boolean>();
        mDrawGraphStates.add(Boolean.valueOf(true));
        mDrawGraphStates.add(Boolean.valueOf(true));
        
        _mainXYPlot = (XYPlot) findViewById(R.id.consumo_mySimpleXYPlot);
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
    	
    	c = getResources().getColor(R.color.red);
    	mColors.add(Integer.valueOf(c));
    	_formatters.add(new LineAndPointFormatter(c, c, null));
    	
    	c = getResources().getColor(R.color.white);
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
        
    	int index = _spinnerTipo.getSelectedItemPosition();
    	if (index == INDICE_ANO ){
    		drawGraphAno();
    	} else if (index == INDICE_MES) {
    		drawGraphMes();
    	} else if (index == INDICE_DIA){
    		drawGraphDia();
    	}
    		
//    	drawGraphDia();
//    	drawGraphMes();
    	
        _mainXYPlot.redraw();
        
        _mainXYPlot.disableAllMarkup();
    }
    
    private void drawGraphDia(){
    	
    	_mainXYPlot.getGraphWidget().setMarginTop(15);
    	_mainXYPlot.getGraphWidget().setMarginRight(10);
    	_mainXYPlot.setPlotPadding(10, 10, 10, 10);
    	_mainXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
    	_mainXYPlot.getLegendWidget().setVisible(false);
    	
    	_mainXYPlot.setTitle("Consumo ao longo do dia");
    	
        for (int i = 0 ; i < _series.size() ; i++){
        	if(mDrawGraphStates.get(i)){
        		_mainXYPlot.addSeries(_series.get(i), _formatters.get(i)); 
        	}
        }
        
    	//sets range formatting
    	_mainXYPlot.setRangeStep(XYStepMode.SUBDIVIDE, 8);
    	_mainXYPlot.setRangeValueFormat(new DecimalFormat("#"));
    	_mainXYPlot.setRangeLabel("Potência consumida (kWh)");
    	_mainXYPlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
    	
    	
    	//sets domain formatting
    	_mainXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
    	_mainXYPlot.setTicksPerDomainLabel(2);
    	_mainXYPlot.setDomainValueFormat(new DecimalFormat("#"));
    	_mainXYPlot.setDomainBoundaries(0, 24, BoundaryMode.FIXED);
    	_mainXYPlot.setDomainLabel("Hora do Dia");
    }
    
private void drawGraphMes(){
    	
    	_mainXYPlot.getGraphWidget().setMarginTop(15);
    	_mainXYPlot.getGraphWidget().setMarginRight(10);
    	_mainXYPlot.setPlotPadding(10, 10, 10, 10);
    	_mainXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
    	_mainXYPlot.getLegendWidget().setVisible(false);
    	
    	
    	_mainXYPlot.setTitle("Consumo ao longo do mes");
    	
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
    	_mainXYPlot.setRangeBoundaries(0, 60, BoundaryMode.FIXED);
    	
    	
    	//sets domain formatting
    	_mainXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
    	_mainXYPlot.setTicksPerDomainLabel(5);
    	_mainXYPlot.setDomainValueFormat(new DecimalFormat("#"));
    	_mainXYPlot.setDomainBoundaries(0, 31, BoundaryMode.FIXED);
    	_mainXYPlot.setDomainLabel("Dia do Mês");
    }


private void drawGraphAno(){
	
	_mainXYPlot.getGraphWidget().setMarginTop(15);
	_mainXYPlot.getGraphWidget().setMarginRight(10);
	_mainXYPlot.setPlotPadding(10, 10, 10, 10);
	_mainXYPlot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
	_mainXYPlot.getLegendWidget().setVisible(false);
	
	
	_mainXYPlot.setTitle("Consumo ao longo do ano");
	
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
	_mainXYPlot.setRangeBoundaries(0, 2000, BoundaryMode.FIXED);
	
	
	//sets domain formatting
	_mainXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
	_mainXYPlot.setTicksPerDomainLabel(1);
	_mainXYPlot.setDomainValueFormat(new DecimalFormat("#"));
	_mainXYPlot.setDomainBoundaries(1, 12, BoundaryMode.FIXED);
	_mainXYPlot.setDomainLabel("Mês do ano");
}
    
    private ArrayList<Residencia> geraDadosResidência(){
    	ArrayList<Residencia> residencias = new ArrayList<Residencia>();
    	Residencia r = new Residencia();
    	r.setNomeCasa("Casa 1");
    	Calendar horaInicio = Calendar.getInstance();
    	Calendar horaTermino = Calendar.getInstance();
    	for (int i = 0 ; i < 48 ; i++){
    		horaInicio = Calendar.getInstance();
    		horaTermino = Calendar.getInstance();
    		horaInicio.set(2012, 10, 11, i/2, (i*30)%60);
    		horaTermino.set(2012, 10, 11, i/2, 15 + (i*30)%60);
    		Log.d(TAG, String.format("Inicio %d:%d", horaInicio.get(Calendar.HOUR_OF_DAY), horaInicio.get(Calendar.MINUTE)));
//    		horaInicio = new Date(2012, 11, 20, i/2, (i*30)%60);
//    		horaTermino = new Date(2012, 11, 20, i/2, 15 + (i*30)%60);
    		r.adicionarMedida(i*3.0, horaInicio, horaTermino); 
    	}
    	residencias.add(r);
    	
    	r = new Residencia();
    	r.setNomeCasa("Casa 2");
    	for (int i = 0 ; i < 20 ; i++){
    		horaInicio = Calendar.getInstance();
    		horaTermino = Calendar.getInstance();
    		horaInicio.set(2012, 10, 11, i/2, (i*30)%60);
    		horaTermino.set(2012, 10, 11, i/2, 15 + (i*30)%60);
    		r.adicionarMedida(30.0 - i, horaInicio, horaTermino); 
    	}
    	residencias.add(r);
    	return residencias;
    }
    
    
    public void dataButtonClicked(View v){
    	final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
    	
    	DatePickerDialog d = new MaxTodayDatePickerDialog(this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
//				mBirthdayView.setText(String.format("%02d/%02d/%04d", monthOfYear + 1, dayOfMonth, year));
//				mFormattedBirthdate = String.format("%04d%02d%02d", year, monthOfYear + 1, dayOfMonth);
//				Log.d(TAG, "new formated birthdate = "+mFormattedBirthdate);
				_datePicked.set(year, monthOfYear, dayOfMonth);
				updateDateText();
				
				geraDataSeries();
				
			}
		}, year, month, day);
    	d.show();
    }
    
    private void setDateLabel(int index){
    	TextView t = ((TextView)findViewById(R.id.consumo_data_label));
    	if (index == INDICE_ANO){
    		t.setText("Ano:");
    	} else if (index == INDICE_MES){
    		t.setText("Mês:");
    	} else if (index == INDICE_DIA){
    		t.setText("Dia:");
    	}
    }
    
    private void geraDataSeries(){
    	int index = _spinnerTipo.getSelectedItemPosition();
    	_series.clear();
    	if (index == INDICE_ANO){
    		geraSeriesConsumoAno();
    	} else if (index == INDICE_MES){
    		geraSeriesConsumoMes();
    	} else if (index == INDICE_DIA){
    		geraSeriesConsumoDia();
    	}
    	
    	drawGraph();
    }
    
    private void geraSeriesConsumoDia(){
    	_series = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		_series.add(r.geraDadosGráficoConsumoData(_datePicked));
    	}
    }
    
    private void geraSeriesConsumoMes(){
    	_series = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		_series.add(r.geraDadosGráficoConsumoMes(_datePicked));
    	}
    }
    
    private void geraSeriesConsumoAno(){
    	_series = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		_series.add(r.geraDadosGráficoConsumoAno(_datePicked));
    	}
    }
    
    private void updateDateText(){
    	TextView t = (TextView)findViewById(R.id.consumo_data_edittext);
    	int index = _spinnerTipo.getSelectedItemPosition();
		if (index == INDICE_ANO){
    		t.setText(String.format("%s", _datePicked.get(Calendar.YEAR)));
    	} else if (index == INDICE_MES){
    		t.setText(String.format("%s/%s", _datePicked.get(Calendar.MONTH)+1, _datePicked.get(Calendar.YEAR)));
    	} else if (index == INDICE_DIA){
    		t.setText(String.format("%s/%s/%s", _datePicked.get(Calendar.DAY_OF_MONTH), _datePicked.get(Calendar.MONTH)+1, _datePicked.get(Calendar.YEAR)));
    	}
    }
}
