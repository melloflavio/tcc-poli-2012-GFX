package com.poli.gfx.model;

import java.util.Date;

import android.util.Log;

public class Medida {
	
	private static final String TAG  = Medida.class.getSimpleName();
	
	private double _potencia;
	private Date _inicioMedida;
	private Date _terminoMedida;
	
	public Medida (double potencia, Date inicio, Date termino){
		_potencia = potencia;
		_inicioMedida = inicio;
		_terminoMedida = termino;
	}
	
	//Getters e Setters
	public double getPotencia() {
		return _potencia;
	}
	public void setPotencia(double potencia) {
		this._potencia = potencia;
	}
	public Date getInicioMedida() {
		return _inicioMedida;
	}
	public void setInicioMedida(Date inicioMedida) {
		this._inicioMedida = inicioMedida;
	}
	public Date getTerminoMedida() {
		return _terminoMedida;
	}
	public void setTerminoMedida(Date terminoMedida) {
		this._terminoMedida = terminoMedida;
	}
	
	// Fim Getters e Setters
	
	public long getDuracaoMedidaEmMinutos(){
		long diff = _terminoMedida.getTime() - _inicioMedida.getTime();
		return diff/(1000 * 60);
	}
	
	public double getConsumoEmkWh(){
//		Log.d(TAG, String.format("Get consumo em kWh pot = %f duracao = %f consumo = %f", _potencia, getDuracaoMedidaEmHoras(), _potencia*getDuracaoMedidaEmHoras() ));
		return _potencia*getDuracaoMedidaEmHoras();
	}
	
	public double getDuracaoMedidaEmHoras(){
		return getDuracaoMedidaEmMinutos()/60.0;
	}
	
	public double getHoraDoDiaInicio(){
		double hora = _inicioMedida.getHours();
		hora += ((double)_inicioMedida.getMinutes())/60.0;
		
		return hora;
	}
}
