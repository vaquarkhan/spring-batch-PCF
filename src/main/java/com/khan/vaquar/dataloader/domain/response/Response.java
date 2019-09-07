package com.khan.vaquar.dataloader.domain.response;

public class Response {

	private Data data;

	private Error error;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "ClassPojo [data = " + data + ", error = " + error + "]";
	}
}
