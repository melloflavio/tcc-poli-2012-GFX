package com.poli.gfx.model;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Medida {
	
	private static final long DURACAO_MEDIDA_MILIS = 15*60*1000;
	
	private static final String TAG  = Medida.class.getSimpleName();
	
	private double _consumo;
	private Calendar _inicioMedida;
	private Calendar _terminoMedida;
	
	public Medida (double consumo, Calendar inicio, Calendar termino){
		_consumo = consumo;
		_inicioMedida = inicio;
		_terminoMedida = termino;
	}
	
	public Medida (double consumo, Calendar inicio){
		_consumo = consumo;
		_inicioMedida = inicio;
	}
	
	//Getters e Setters
	public double getPotencia() {
		return _consumo;
	}
	public void setPotencia(double potencia) {
		this._consumo = potencia;
	}
	public Calendar getInicioMedida() {
		return _inicioMedida;
	}
	public void setInicioMedida(Calendar inicioMedida) {
		this._inicioMedida = inicioMedida;
	}
	public Calendar getTerminoMedida() {
		return _terminoMedida;
	}
	public void setTerminoMedida(Calendar terminoMedida) {
		this._terminoMedida = terminoMedida;
	}
	
	// Fim Getters e Setters
	
	public long getDuracaoMedidaEmMinutos(){
//		long diff = _terminoMedida.getTimeInMillis() - _inicioMedida.getTimeInMillis();
//		return diff/(1000 * 60);
		return DURACAO_MEDIDA_MILIS*60*1000;
	}
	
	public double getConsumoEmkWh(){
//		Log.d(TAG, String.format("Get consumo em kWh pot = %f duracao = %f consumo = %f", _potencia, getDuracaoMedidaEmHoras(), _potencia*getDuracaoMedidaEmHoras() ));
//		return _consumo*getDuracaoMedidaEmHoras();
		return _consumo;
	}
	
	public double getDuracaoMedidaEmHoras(){
		return getDuracaoMedidaEmMinutos()/60.0;
	}
	
	public double getHoraDoDiaInicio(){
		
		double hora =(double) _inicioMedida.get(Calendar.HOUR_OF_DAY);
		hora += ((double)_inicioMedida.get(Calendar.MINUTE))/60.0;
		return hora;
	}
}
