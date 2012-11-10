package com.poli.gfx.model;

import java.util.ArrayList;
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
	
	public void adicionarMedida(double potencia, Date inicio, Date termino){
		_medidas.add(new Medida(potencia, inicio, termino));
		Collections.sort(_medidas, _comparador);
	}
	
	public XYSeries geraDadosGráficoConsumoData(Date data){
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
	
	private ArrayList<Medida> getMedidasData(Date data){
		ArrayList<Medida> medidas = new ArrayList<Medida>();
		Date dataMedida;
		
		for (Medida m : _medidas){
			dataMedida = m.getInicioMedida();
			if (data.getDate() == dataMedida.getDate() && data.getMonth() == dataMedida.getMonth() && data.getYear() == dataMedida.getYear()){
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
}
