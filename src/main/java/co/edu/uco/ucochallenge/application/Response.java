package co.edu.uco.ucochallenge.application;

public abstract class Response<T>{
	
	private boolean dataReturned;
	private T data;
	
	protected Response(final boolean dataReturned, final T data) {
		setDataReturned(dataReturned);
		setData(data);
	}

	private void setDataReturned(boolean returnData) {
		//Limpieza de datos
		this.dataReturned = returnData;
	}

	private void setData(T data) {
		//Limpieza de datos
		this.data = data;
	}

	public boolean isDataReturned() {
		return dataReturned;
	}

	public T getData() {
		return data;
	}
	
	
	
	
}
