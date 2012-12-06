package com.poli.gfx.main;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
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
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.Plot;
import com.androidplot.xy.XYStepMode;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.poli.gfx.R;
import com.poli.gfx.model.Residencia;
import com.poli.gfx.util.AppHttpClient;
import com.poli.gfx.util.Paths;
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
	private ListAdapter _listAdapter;
	
	
	private ArrayList<Integer> mColors = new ArrayList<Integer>();
	private ArrayList<LineAndPointFormatter> _formatters = new ArrayList<LineAndPointFormatter>(); 
	private ArrayList<Residencia> _residencias;
	private ArrayList<XYSeries> _series = new ArrayList<XYSeries>();
	
	private XYPlot _mainXYPlot;
	
	private Spinner _spinnerTipo;
	
	private final String[] tiposDeGrafico = {"Anual", "Mensal", "Diário"};
	
	private Calendar _datePicked;
	
	private ProgressDialog mProgressDialog;
	
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
				setDateLabel(position);
				updateDateText();
//				geraDataSeries();
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
		});
        
//        mHouseNames = new ArrayList<String>();
        
        
        
        
//        getHousesFromSharedPrefs();
        setColors();
        
        
        mListView = (ListView) findViewById(R.id.consumo_listView);
        _listAdapter = new ListAdapter();
        mListView.setAdapter(_listAdapter);
        
//        drawGraph();
    }
    
    
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	showProgressDialog("Carregando Dados");
//    	geraSeriesConsumoDia();
    	//Testing
        //TODO
//        _residencias = geraDadosResidência();
    	_residencias = getResidencesFromSharedPrefs();
//    	requestPropertyInfo("1");
        
    	hideProgressDialog();
    	
//    	drawGraph();
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
        
        _residencias = new ArrayList<Residencia>();
        
        //TODO
        mDrawGraphStates = new ArrayList<Boolean>();
        mDrawGraphStates.add(Boolean.valueOf(true));
        mDrawGraphStates.add(Boolean.valueOf(true));
        mDrawGraphStates.add(Boolean.valueOf(true));
        mDrawGraphStates.add(Boolean.valueOf(true));
        
        _mainXYPlot = (XYPlot) findViewById(R.id.consumo_mySimpleXYPlot);
    }
    
    private void requestPropertyDataDay(final String houseId, final Calendar datePicked, final int index) {
    	RequestParams params = new RequestParams();
    	params.put("houseId", houseId);
    	params.put("date", String.format("%s-%s-%s", _datePicked.get(Calendar.YEAR), _datePicked.get(Calendar.MONTH)+1, _datePicked.get(Calendar.DAY_OF_MONTH)));
    	params.put("random", String.format("%f", new Random().nextFloat()));
    	
    	Log.d(TAG, "houseId = "+houseId+"params = "+params.toString());
    	
    	AppHttpClient.get(Paths.GET_HOUSE_MEDIDAS_DIA, params, new JsonHttpResponseHandler(){
//    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new AsyncHttpResponseHandler(){
    		@Override
    		public void onStart() {
    	    	showProgressDialog("Caregando Medidas casa #"+houseId);
    			Log.d(TAG, "Start");
    		}
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			Log.d(TAG, "Success = "+response.toString());
    			
    			boolean success;
    			JSONArray medidasJSON;
				try {
					success = response.getBoolean("status");
					medidasJSON = response.getJSONArray("medidas");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					medidasJSON = null;
					e.printStackTrace();
				}
				if(success){
					hideProgressDialog();
					parseMedidasDia(medidasJSON, houseId, index);
					
					
				}
				else{
					showHouseRequestErrorDialog();
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "fail");
    			showHouseRequestErrorDialog();
    		}
    		
    		
    		@Override
			public void onFailure(Throwable e, JSONObject response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response.toString());
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response);
    		}
    		
    		@Override
			public void onFailure(Throwable e, JSONArray response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail array  response = "+response.toString());
    		}
    		
    		@Override
    		public void onFinish() {
//    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
    }
    
    private void requestPropertyDataMonth(final String houseId, final Calendar datePicked, final int index) {
    	RequestParams params = new RequestParams();
    	params.put("houseId", houseId);
    	params.put("date", String.format("%s-%s", _datePicked.get(Calendar.YEAR), _datePicked.get(Calendar.MONTH)+1));
    	params.put("random", String.format("%f", new Random().nextFloat()));
    	
    	Log.d(TAG, "houseId = "+houseId+"params = "+params.toString());
    	
    	AppHttpClient.get(Paths.GET_HOUSE_MEDIDAS_MES, params, new JsonHttpResponseHandler(){
//    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new AsyncHttpResponseHandler(){
    		@Override
    		public void onStart() {
    	    	showProgressDialog("Caregando Medidas casa #"+houseId);
    			Log.d(TAG, "Start");
    		}
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			Log.d(TAG, "Success = "+response.toString());
    			
    			boolean success;
    			JSONArray medidasJSON;
				try {
					success = response.getBoolean("status");
					medidasJSON = response.getJSONArray("medidas");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					medidasJSON = null;
					e.printStackTrace();
				}
				if(success){
					hideProgressDialog();
					parseMedidasMes(medidasJSON, houseId, index);
					
					
				}
				else{
					showHouseRequestErrorDialog();
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "fail");
    			showHouseRequestErrorDialog();
    		}
    		
    		
    		@Override
			public void onFailure(Throwable e, JSONObject response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response.toString());
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response);
    		}
    		
    		@Override
			public void onFailure(Throwable e, JSONArray response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail array  response = "+response.toString());
    		}
    		
    		@Override
    		public void onFinish() {
//    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
    }
    
    private void requestPropertyDataYear(final String houseId, final Calendar datePicked, final int index) {
    	RequestParams params = new RequestParams();
    	params.put("houseId", houseId);
    	params.put("date", String.format("%s-01", _datePicked.get(Calendar.YEAR)));
    	params.put("random", String.format("%f", new Random().nextFloat()));
    	
    	Log.d(TAG, "houseId = "+houseId+"params = "+params.toString());
    	
    	AppHttpClient.get(Paths.GET_HOUSE_MEDIDAS_ANO, params, new JsonHttpResponseHandler(){
//    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new AsyncHttpResponseHandler(){
    		@Override
    		public void onStart() {
    	    	showProgressDialog("Caregando Medidas casa #"+houseId);
    			Log.d(TAG, "Start");
    		}
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			Log.d(TAG, "Success = "+response.toString());
    			
    			boolean success;
    			JSONArray medidasJSON;
				try {
					success = response.getBoolean("status");
					medidasJSON = response.getJSONArray("medidas");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					medidasJSON = null;
					e.printStackTrace();
				}
				if(success){
					hideProgressDialog();
					parseMedidasAno(medidasJSON, houseId, index);
					
					
				}
				else{
					showHouseRequestErrorDialog();
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "fail");
    			showHouseRequestErrorDialog();
    		}
    		
    		
    		@Override
			public void onFailure(Throwable e, JSONObject response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response.toString());
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response);
    		}
    		
    		@Override
			public void onFailure(Throwable e, JSONArray response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail array  response = "+response.toString());
    		}
    		
    		@Override
    		public void onFinish() {
//    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
    }
    
    //Request
 /*   private void requestPropertyInfo(final String houseId) {
    	RequestParams params = new RequestParams();
    	params.put("houseId", houseId);
    	params.put("random", String.format("%f", new Random().nextFloat()));
    	
    	Log.d(TAG, "houseId = "+houseId+"params = "+params.toString());
    	
    	AppHttpClient.get(Paths.GET_ALL_HOUSE_MEDIDAS, params, new JsonHttpResponseHandler(){
//    	AppHttpClient.post(Paths.GET_ACCOUNT, params, new AsyncHttpResponseHandler(){
    		@Override
    		public void onStart() {
    	    	showProgressDialog("Caregando Medidas casa #"+houseId);
    			Log.d(TAG, "Start");
    		}
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			Log.d(TAG, "Success = "+response.toString());
    			
    			boolean success;
    			JSONArray medidasJSON;
				try {
					success = response.getBoolean("status");
					Log.d(TAG, "1");
					medidasJSON = response.getJSONArray("medidas");
					Log.d(TAG, "2");
				} catch (JSONException e) {
					//TODO error receiving message from server
					success = false;//Default value
					medidasJSON = null;
					e.printStackTrace();
				}
				if(success){
					Log.d(TAG, "3");
					hideProgressDialog();
					parseMedidas(medidasJSON, houseId);
					Log.d(TAG, "4");
					
				}
				else{
					showHouseRequestErrorDialog();
				}
    		}
    		
    		@Override
			public void onFailure(Throwable e) {
    			Log.d(TAG, "fail");
    			showHouseRequestErrorDialog();
    		}
    		
    		
    		@Override
			public void onFailure(Throwable e, JSONObject response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response.toString());
    		}
    		
    		@Override
			public void onFailure(Throwable e, String response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail object  response = "+response);
    		}
    		
    		@Override
			public void onFailure(Throwable e, JSONArray response) {
    			showHouseRequestErrorDialog();
    			Log.d(TAG, "Fail array  response = "+response.toString());
    		}
    		
    		@Override
    		public void onFinish() {
//    			hideProgressDialog();
    			Log.d(TAG, "Finish");
    		}
    	});
    }
    */
    
    
    private void getHousesFromSharedPrefs(){
    	int houseCount = SharedPreferencesAdapter.getIntFromSharedPreferences(SharedPreferencesAdapter.HOUSE_COUNT_KEY, getApplicationContext());
        
    	String houseName;
    	String houseId;
    	
        for (int i = 0 ; i < houseCount ; i++){
        	houseName = SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_NAME_KEY+i, getApplicationContext());
        	houseId= SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+i, getApplicationContext());
        	
        	if(houseName != null && houseId != null){
//        		Log.d(TAG, "house found! id = "+houseId+" name = "+houseName);
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
//			Log.d(TAG, String.format("Position = %d colors size = %d", position, mColors.size()));
			((TextView)convertView.findViewById(R.id.consumo_list_color)).setBackgroundColor(mColors.get(position).intValue());
			((CheckBox)convertView.findViewById(R.id.consumo_list_checkBox)).setChecked(true);
			
			convertView.setClickable(true);
			
			convertView.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
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
//    	_mainXYPlot.setRangeBoundaries(0, 8, BoundaryMode.FIXED);
//    	_mainXYPlot.setRangeBoundaries(0, 12, BoundaryMode.FIXED);
    	
    	
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
//    	_mainXYPlot.setRangeBoundaries(0, 60, BoundaryMode.FIXED);
    	
    	
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
	_mainXYPlot.setRangeBoundaries(0, 60000, BoundaryMode.FIXED);
//	_mainXYPlot.setRangeBoundaries(0, 2000, BoundaryMode.FIXED);
	
	
	//sets domain formatting
	_mainXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
	_mainXYPlot.setTicksPerDomainLabel(1);
	_mainXYPlot.setDomainValueFormat(new DecimalFormat("#"));
	_mainXYPlot.setDomainBoundaries(1, 12, BoundaryMode.FIXED);
	_mainXYPlot.setDomainLabel("Mês do ano");
}
    
    private ArrayList<Residencia> geraDadosResidência(){
    	//For TESTING
    	Random random = new Random();
    	ArrayList<Residencia> residencias = new ArrayList<Residencia>();
    	Residencia r = new Residencia();
    	r.setNomeCasa("EPUSP");
    	Calendar horaInicio = Calendar.getInstance();
    	Calendar horaTermino = Calendar.getInstance();
	    for (int dia = 1 ; dia < 31 ; dia++){
    		for (int i = 0 ; i <= 48 ; i++){
	    		horaInicio = Calendar.getInstance();
	    		horaTermino = Calendar.getInstance();
	    		horaInicio.set(2012, 10, dia, i/2, (i*30)%60);
	    		horaTermino.set(2012, 10, dia, i/2, 15 + (i*30)%60);
	    		
//	    		r.adicionarMedida(i*(5.0*2.0)/48.0, horaInicio, horaTermino);
//	    		r.adicionarMedida(random.nextDouble()*7, horaInicio, horaTermino);
	    		if (i <= 16){
	    			r.adicionarMedida(1 + (Math.sin((((double) i)/20.0d)*Math.PI - Math.PI/2.0f)*2+3)/2.0d  + (0.5 -  random.nextDouble())/2.0f, horaInicio, horaTermino);
	    		} else if (i <= 30) {
	    			r.adicionarMedida(1 + 1.3f + (0.5 -  random.nextDouble())/1.5f, horaInicio, horaTermino);
	    		} else if (i <= 38){
	    			r.adicionarMedida(1 + Math.sin((((double) i)/20.0d)*4*Math.PI - 4.0/3.0*Math.PI)*2+2 + (0.5 -  random.nextDouble()), horaInicio, horaTermino);
	    		} else {
	    			r.adicionarMedida(1 + (50 - i)/6.0f + (0.5 -  random.nextDouble())/2.0f, horaInicio, horaTermino);
	    		}
	    	}
    	}
    	residencias.add(r);
    	
    	r = new Residencia();
    	r.setNomeCasa("CEE");
    	
    	for (int dia = 1 ; dia < 31 ; dia++){
    		for (int i = 0 ; i <= 48 ; i++){
	    		horaInicio = Calendar.getInstance();
	    		horaTermino = Calendar.getInstance();
	    		horaInicio.set(2012, 10, dia, i/2, (i*30)%60);
	    		horaTermino.set(2012, 10, dia, i/2, 15 + (i*30)%60);
	    		
//	    		r.adicionarMedida(5 - i*(5.0*2.0)/48.0, horaInicio, horaTermino); 
//	    		r.adicionarMedida((6%((i/2)+1)) + (0.5 -  random.nextDouble()), horaInicio, horaTermino);
	    		if (i <= 12){
	    			r.adicionarMedida((Math.sin((((double) i)/20.0d)*Math.PI - Math.PI/2.0f)*2+3)/2.0d  + (0.5 -  random.nextDouble())/2.0f, horaInicio, horaTermino);
	    		} else if (i > 12 && i <= 34) {
	    			r.adicionarMedida(1.3f + (0.5 -  random.nextDouble())/2.0f, horaInicio, horaTermino);
	    		} else if (i > 34 && i <= 42){
	    			r.adicionarMedida(Math.sin((((double) i)/20.0d)*4*Math.PI - 4.0/3.0*Math.PI)*2+2 + (0.5 -  random.nextDouble())/2.0f, horaInicio, horaTermino);
	    		} else {
	    			r.adicionarMedida((50 - i)/5.0f + (0.5 -  random.nextDouble())/2.0f, horaInicio, horaTermino);
	    		}
	    		
	    	}
    	}
    	residencias.add(r);
    	return residencias;
    }
    
    
    public void dataButtonClicked(View v){
//    	final Calendar c = Calendar.getInstance();
		int year = _datePicked.get(Calendar.YEAR);
		int month = _datePicked.get(Calendar.MONTH);
		int day = _datePicked.get(Calendar.DAY_OF_MONTH);
    	
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
    
    public void getGraphButtonClicked(View v){
    	int index = _spinnerTipo.getSelectedItemPosition();
    	if (index == INDICE_ANO ){
//    		requestPropertyDataYear();
    		for(int i = 0 ; i < _residencias.size() ; i++ ){
    			Residencia r = _residencias.get(i);
    			requestPropertyDataYear(r.getHouseId(), _datePicked, i);
    		}
    	} else if (index == INDICE_MES) {
//    		requestPropertyDataMonth();
    		for(int i = 0 ; i < _residencias.size() ; i++ ){
    			Residencia r = _residencias.get(i);
    			requestPropertyDataMonth(r.getHouseId(), _datePicked, i);
    		}
    	} else if (index == INDICE_DIA){
    		for(int i = 0 ; i < _residencias.size() ; i++ ){
    			Residencia r = _residencias.get(i);
    			requestPropertyDataDay(r.getHouseId(), _datePicked, i);
    		}
    	}
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
    		_series = geraSeriesConsumoAno();
    	} else if (index == INDICE_MES){
    		_series = geraSeriesConsumoMes();
    	} else if (index == INDICE_DIA){
    		_series = geraSeriesConsumoDia();
    	}
    	
//    	drawGraph();
    }
    
    private ArrayList<XYSeries> geraSeriesConsumoDia(){
    	ArrayList<XYSeries> s = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		s.add(r.geraDadosGráficoConsumoData(_datePicked));
    	}
    	
    	return s;
    }
    
    private ArrayList<XYSeries> geraSeriesConsumoMes(){
    	ArrayList<XYSeries> s = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		s.add(r.geraDadosGráficoConsumoMes(_datePicked));
    	}
    	
    	return s;
    }
    
    private ArrayList<XYSeries> geraSeriesConsumoAno(){
    	ArrayList<XYSeries> s = new ArrayList<XYSeries>();
    	Residencia r;
    	for (int i = 0 ; i < _residencias.size() ; i++){
    		r = _residencias.get(i);
    		s.add(r.geraDadosGráficoConsumoAno(_datePicked));
    	}
    	
    	return s;
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
    
  //Dialogs
    private void showProgressDialog(String mensagem){
    	if(mProgressDialog != null && mProgressDialog.isShowing())
    	{
    		mProgressDialog.dismiss();
    	}
    	mProgressDialog = ProgressDialog.show(this, "", mensagem);
    }
    
    private void hideProgressDialog(){
    	Log.d(TAG, "Hiding Progress Dialog");
    	mProgressDialog.dismiss();
    }
    
    private void showHouseRequestErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ConsumoActivity.this);
		builder.setTitle("Erro de Conexao")
		.setMessage("Erro ao receber informações da residência.")
		.setPositiveButton("Ok", null)
		.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}
    
    
    private void parseMedidasDia(JSONArray medidas, String houseId, int index){
    	SimpleXYSeries serie = new SimpleXYSeries(_residencias.get(index).getNomeCasa());
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	double valor;
    	double hora;
    	Calendar c;
    	String data;
    	for (int i = 0 ; i < medidas.length() ; i++) {
    		try {
				valor = ((JSONObject) medidas.get(i)).getDouble("consumo");
				data = ((JSONObject) medidas.get(i)).getString("inicio");
			} catch (JSONException e) {
				valor = -1;
				data = null;
				e.printStackTrace();
			}
    		c = Calendar.getInstance();
    		try {
				c.setTime(format.parse(data));
			} catch (ParseException e) {
				c = null;
				e.printStackTrace();
			}
    		
    		hora = c.get(Calendar.HOUR_OF_DAY) + ((double)c.get(Calendar.MINUTE))/60.0d;
    		
    		Log.d(TAG, "Medida: hora="+hora+" consumo = "+valor+"\n");
    		serie.addFirst(hora, valor);
    	}
    	
    	_series.add(index, serie);
    	drawGraph();
    }
    
    private void parseMedidasMes(JSONArray medidas, String houseId, int index){
    	SimpleXYSeries serie = new SimpleXYSeries(_residencias.get(index).getNomeCasa());
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	
    	double valor;
    	double dia;
    	Calendar c;
    	String data;
    	for (int i = 0 ; i < medidas.length() ; i++) {
    		try {
				valor = ((JSONObject) medidas.get(i)).getDouble("consumo");
				data = ((JSONObject) medidas.get(i)).getString("inicio");
			} catch (JSONException e) {
				valor = -1;
				data = null;
				e.printStackTrace();
			}
    		c = Calendar.getInstance();
    		try {
				c.setTime(format.parse(data));
			} catch (ParseException e) {
				c = null;
				e.printStackTrace();
			}
    		
    		dia = c.get(Calendar.DAY_OF_MONTH);
    		
    		Log.d(TAG, "Medida: dia="+dia+" consumo = "+valor+"\n");
    		serie.addFirst(dia, valor);
    	}
    	
    	_series.add(index, serie);
    	drawGraph();
    }
    
    private void parseMedidasAno(JSONArray medidas, String houseId, int index){
    	SimpleXYSeries serie = new SimpleXYSeries(_residencias.get(index).getNomeCasa());
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	
    	double valor;
    	double mes;
    	Calendar c;
    	String data;
    	for (int i = 0 ; i < medidas.length() ; i++) {
    		try {
				valor = ((JSONObject) medidas.get(i)).getDouble("consumo");
				data = ((JSONObject) medidas.get(i)).getString("inicio");
			} catch (JSONException e) {
				valor = -1;
				data = null;
				e.printStackTrace();
			}
    		c = Calendar.getInstance();    		
    		try {
				c.setTime(format.parse(data));
			} catch (ParseException e) {
				c = null;
				e.printStackTrace();
			}
    		
    		
    		mes = c.get(Calendar.MONTH);
    		Log.d(TAG, "Medida: mes="+mes+" consumo = "+valor+"\n");
    		serie.addFirst(mes, valor);
    	}
    	
    	_series.add(index, serie);
    	drawGraph();
    }
    
//    Versão Velha
    /*private void parseMedidas(JSONArray medidas, String houseId){
    	showProgressDialog("Processando dados");
    	
    	SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    	
    	Residencia r = new Residencia();
    	r.setNomeCasa("Casa "+houseId);
    	
    	int length = medidas.length();
    	for (int i = 0; i < length ; i ++){
    		JSONObject obj;
    		double consumo;
    		Calendar inicioMedida;
    		String inicioMedidaString;
			try {
				obj = medidas.getJSONObject(i);
				consumo = obj.getDouble("c");
				inicioMedidaString = obj.getString("i");
			} catch (JSONException e) {
				consumo = -1;
				inicioMedidaString = null;
				e.printStackTrace();
			}
			
			try {
				Date d = form.parse(inicioMedidaString);
				inicioMedida = Calendar.getInstance();
				inicioMedida.setTime(d);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				inicioMedida = null;
				e.printStackTrace();
			}
			
			if (!(consumo < 0) &&  inicioMedida != null){
				r.adicionarMedida(consumo, inicioMedida);
			}
//			if(currHouseId != null){
				SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+i, currHouseId, getApplicationContext());
				SharedPreferencesAdapter.storeStringToSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_NAME_KEY+i, currHouseName, getApplicationContext());
//			}
    	}
    	
    	_residencias.add(r);
		_listAdapter.notifyDataSetChanged();
		
		hideProgressDialog();
		geraDataSeries();
    }*/
    
    private ArrayList<Residencia> getResidencesFromSharedPrefs() {
    	ArrayList<Residencia> residencias = new ArrayList<Residencia>();
    	int nResidencias = SharedPreferencesAdapter.getIntFromSharedPreferences(SharedPreferencesAdapter.HOUSE_COUNT_KEY, getApplicationContext());
    	Residencia r;
    	for (int i = 0 ; i < nResidencias ; i++){
    		r = new Residencia();
    		r.setNomeCasa(SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_NAME_KEY+i, getApplicationContext()));
    		r.setHouseId(SharedPreferencesAdapter.getStringFromSharedPreferences(SharedPreferencesAdapter.BASE_HOUSE_ID_KEY+i, getApplicationContext()));
    		residencias.add(r);
    	}
    	
    	return residencias;
    }
}
