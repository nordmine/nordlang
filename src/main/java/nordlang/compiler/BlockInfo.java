package nordlang.compiler;

import nordlang.vm.TwinValue;

public class BlockInfo {

	private TwinValue firstAddress;
	private TwinValue secondAddress;

	public TwinValue getFirstAddress() {
		return firstAddress;
	}

	public void setFirstAddress(TwinValue firstAddress) {
		this.firstAddress = firstAddress;
	}

	public TwinValue getSecondAddress() {
		return secondAddress;
	}

	public void setSecondAddress(TwinValue secondAddress) {
		this.secondAddress = secondAddress;
	}
}
