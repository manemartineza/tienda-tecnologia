package dominio;

import dominio.repositorio.RepositorioProducto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Vendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    public static final String  EL_PRODUCTO_NO_GARANTIA_EXTENDIDA = "Este producto no cuenta con garantía extendida";

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }

    public void generarGarantia(String codigo,String nombre,double precio,String nombreCliente) {
        //throw new UnsupportedOperationException("Método pendiente por implementar");    	
    	if( tieneTresVocales(codigo) ) {
    		throw new GarantiaExtendidaException(EL_PRODUCTO_NO_GARANTIA_EXTENDIDA);
    	}
    	
    	if( tieneGarantia(codigo) ) {
    		throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
    	}    	
    	
    	boolean cumple = (precio > 500000) ? true : false;    	
    	
    	Producto producto = new Producto(codigo,nombre, precio);    	
    	GarantiaExtendida garantia = new GarantiaExtendida( producto,fechaSolicitudGarantia() ,fechaFinGarantía(cumple),precioGarantía(precio,cumple),nombreCliente);    	
    	this.repositorioGarantia.agregar(garantia);
    	
    
    }

    public boolean tieneGarantia(String codigo) {
    	
        return this.repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo) != null ;
    }
    
    public boolean tieneTresVocales(String codigo ) {
    	
    	String vocalesCadena = "AEIOU";
    	boolean tiene = false;
    	int cantidad = 0;
    	int longitud = codigo.length();
    	
    	for (int p = 0; p < longitud; p++) {
    		 char caracter 	 = codigo.charAt(p);    		     		 
    		 
    		 if( vocalesCadena.indexOf(caracter) != -1 ) {
    			 cantidad++;
    		 }
    	}
    	
    	if( cantidad >= 3 ) {
    		tiene = true;
    	}
    	
    	return tiene;
    }
    
    public Date  fechaSolicitudGarantia() {
    	Date objDate = new Date();
    	return objDate;
    }
    
    public Date fechaFinGarantía(boolean cumple) {
    	
    	String fecha,strFechaFormato,strDiaDormato;
    	int diasASumar,i,diasAdelante,dias;
    	Date fechaActual,fechaRetornar;
    	Calendar c;
    	 	
    	dias = (cumple) ? 199 : 99 ;
    	i = diasASumar =  diasAdelante = 1;
    	
    	fecha = "";  
    	strFechaFormato = "dd/MM/yyyy"; 
    	strDiaDormato = "E";
    	fechaActual = new Date();
    	fechaRetornar = null;
    	
    	
    	while ( i <= dias) {
    			
    		c = Calendar.getInstance();
        	c.setTime(new Date());
        	c.add(Calendar.DAY_OF_YEAR , diasAdelante );
        	
        	Date currentDatePlusOne = c.getTime();
        	SimpleDateFormat objSDF = new SimpleDateFormat(strFechaFormato); 
        	SimpleDateFormat objSDia = new SimpleDateFormat(strDiaDormato); 
        	
        	String diaSemana = objSDia.format(currentDatePlusOne);
        	fecha 			 = objSDF.format(currentDatePlusOne);
        	
        	
        	if( !diaSemana.equals("lun") ) {        		
        		
        		if( i == dias ) {        			
        			
        			if( !diaSemana.equals("dom") && !diaSemana.equals("lun") ) 
        				i++;
        			else 
        				i--;
        			
        		}else {
        			i++;
        		}       		
        		
        	}else {
        		i--;
        	}
        	
        	
        	diasAdelante++;
    		
    	}
    	
    	try {
			fechaRetornar = new SimpleDateFormat(strFechaFormato).parse(fecha);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	
    	return fechaRetornar;
    }
    
    public double precioGarantía(double precio,boolean cumple ) {
    	double precioGarantía = 0;
    	double  porcentaje = (cumple) ? 0.20 : 0.10;
    	
    	precioGarantía =   precio * porcentaje;
    	
    	return precioGarantía;
    }
    
    

}
