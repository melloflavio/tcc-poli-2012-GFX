package com.poli.gfx.util;

public class Paths {
	
//	public static final String BASE_URL = "http://192.168.1.2/TCC/tcc-poli-2012-GFX/webservice/"; 
//	public static final String BASE_URL = "http://192.168.1.3/TCC/tcc-poli-2012-GFX/webservice/";
	public static final String BASE_URL = "http://192.168.1.130/TCC/tcc-poli-2012-GFX/webservice/";
//	public static final String BASE_URL = "http://192.168.1.21/TCC/tcc-poli-2012-GFX/webservice/";
	
	public static final String GET_ACCOUNT = "getGFXAccount"; //Params: "email" , "password"
	public static final String GET_HOUSE_INFO = "getHouseInfo"; //Params: "houseId"
//	public static final String GET_ALL_HOUSE_MEDIDAS = "getAllMedidas"; //Params "houseId"
	public static final String GET_HOUSE_MEDIDAS_DIA = "getMedidasDia"; //Params "houseId" "date"
	public static final String GET_HOUSE_MEDIDAS_MES = "getMedidasMes"; //Params "houseId" "date"
	public static final String GET_HOUSE_MEDIDAS_ANO = "getMedidasAno"; //Params "houseId" "date"
}
