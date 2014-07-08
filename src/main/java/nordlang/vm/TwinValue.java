package nordlang.vm;

/**
 * Тип данных, позволяющий хранить числовое или строковое значение.
 * Значение одного из двух полей всегда null.
 */
public class TwinValue {

	private String strVal;
	// преждевременно: избавиться от Integer в пользу примитивного типа
	private Integer intVal;

	public TwinValue(String strVal, Integer intVal) {
		this.strVal = strVal;
		this.intVal = intVal;
	}

	public TwinValue(String strValue) {
		this.strVal = strValue;
	}

	public TwinValue(Integer intValue) {
		this.intVal = intValue;
	}

	public String getStrVal() {
		return strVal;
	}

	public void setStrVal(String strVal) {
		this.intVal = null;
		this.strVal = strVal;
	}

	public Integer getIntVal() {
		return intVal;
	}

	public void setIntVal(Integer intVal) {
		this.strVal = null;
		this.intVal = intVal;
	}

	@Override
	public String toString() {
		return intVal == null ? strVal : intVal.toString();
	}

	/**
	 * Создаёт полную копию объекта
	 *
	 * @return
	 */
	public TwinValue copy() {
		return new TwinValue(strVal, intVal);
	}
}
