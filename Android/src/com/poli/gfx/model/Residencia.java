package com.poli.gfx.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.util.Log;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.SimpleXYSeries;

public class Residencia {

	private static final String TAG = Residencia.class.getSimpleName();
	
	private String _houseId;
	private String _nomeCasa;
	private String _logradouro;
	private String _cidade;
	private String _estado;
	private ArrayList<Medida> _medidas;
	private ComparadorMedidas _comparador;
	
	public Residencia(){
		_medidas = new ArrayList<Medida>();
		_comparador = new ComparadorMedidas();
	}
	
	public String getHouseId() {
		return _houseId;
	}
	public void setHouseId(String houseId) {
		this._houseId = houseId;
	}
	public String getNomeCasa() {
		return _nomeCasa;
	}
	public void setNomeCasa(String nomeCasa) {
		this._nomeCasa = nomeCasa;
	}
	public String getLogradouro() {
		return _logradouro;
	}
	public void setLogradouro(String logradouro) {
		this._logradouro = logradouro;
	}
	public String getCidade() {
		return _cidade;
	}
	public void setCidade(String cidade) {
		this._cidade = cidade;
	}
	public String getEstado() {
		return _estado;
	}
	public void setEstado(String estado) {
		this._estado = estado;
	}
	
	public void adicionarMedida(double potencia, Calendar inicio, Calendar termino){
		_medidas.add(new Medida(potencia, inicio, termino));
		Collections.sort(_medidas, _comparador);
	}
	
	public XYSeries geraDadosGráficoConsumoData(Calendar data){
		ArrayList<Medida> medidasDoDia = getMedidasData (data);
		Medida m;
		Collections.sort(medidasDoDia, _comparador);
		SimpleXYSeries series = new SimpleXYSeries(_nomeCasa);
		
		
//		ArrayList<Double> horas = new ArrayList<Double>();
//		ArrayList<Double> consumo = new ArrayList<Double>();
		for (int i = 0 ; i < medidasDoDia.size() ; i++){
			m = medidasDoDia.get(i);
//			series.setX(m.getHoraDoDiaInicio(), i);
//			series.setY(m.getConsumoEmkWh(), i);
//			horas.add(new Double(m.getHoraDoDiaInicio()));
//			consumo.add(new Double(m.getConsumoEmkWh()));
			series.addFirst(new Double(m.getHoraDoDiaInicio()),new Double(m.getConsumoEmkWh()));
		}
//		SimpleXYSeries series = new SimpleXYSeries(horas, consumo, _nomeCasa);
		Log.d(TAG, String.format("Gerando dados casa= %s tamanho = %d", _nomeCasa, medidasDoDia.size()));
		return series;
	}
	
	private ArrayList<Medida> getMedidasData(Calendar data){
		ArrayList<Medida> medidas = new ArrayList<Medida>();
		Calendar dataMedida;
		
		for (Medida m : _medidas){
			dataMedida = m.getInicioMedida();
			if (data.get(Calendar.DAY_OF_MONTH) == dataMedida.get(Calendar.DAY_OF_MONTH) && data.get(Calendar.MONTH) == dataMedida.get(Calendar.MONTH) && data.get(Calendar.YEAR) == dataMedida.get(Calendar.YEAR)){
				medidas.add(m);
			}
		}
		return medidas;
	}
	
	private class ComparadorMedidas implements Comparator<Medida>{

		public int compare(Medida m1, Medida m2) {
			return m1.getInicioMedida().compareTo(m2.getInicioMedida());
		}
		
	}
	
	
	//Medidas do mes
	public XYSeries geraDadosGráficoConsumoMes(Calendar data){
		//TODO
		
		
		
		
		ArrayList<Medida> medidasDoDia;
		Medida m;
		SimpleXYSeries series = new SimpleXYSeries(_nomeCasa);
		double consumoDia;
		
//		?for (int i = 0 ; i < medidasDoMes.size() ; i++){
//			m = medidasDoMes.get(i);
//			series.addFirst(new Double(m.getHoraDoDiaInicio()),new Double(m.getConsumoEmkWh()));
//		}
//		Log.d(TAG, String.format("Gerando dados casa= %s tamanho = %d", _nomeCasa, medidasDoDia.size()));
		return series;
	}
	
	//Medidas do mes
		public XYSeries geraDadosGráficoConsumoAno(Calendar data){
			//TODO
			
			
			
			ArrayList<Medida> medidasDoDia;
			Medida m;
			SimpleXYSeries series = new SimpleXYSeries(_nomeCasa);
			double consumoDia;
			
//			?for (int i = 0 ; i < medidasDoMes.size() ; i++){
//				m = medidasDoMes.get(i);
//				series.addFirst(new Double(m.getHoraDoDiaInicio()),new Double(m.getConsumoEmkWh()));
//			}
//			Log.d(TAG, String.format("Gerando dados casa= %s tamanho = %d", _nomeCasa, medidasDoDia.size()));
			return series;
		}
	
}
