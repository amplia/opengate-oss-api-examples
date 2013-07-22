package entity

class FilterCounterComparer {
	
	private static final String EMPTY_FILTER = "emptyFilter"
	int itemsNumberEmptyFilter
	int itemsNumberEqualsFilter
	int itemsNumberNotEquals

	public boolean isCorrectCounter(){
		return itemsNumberEmptyFilter==(itemsNumberEqualsFilter+itemsNumberNotEquals)
	}
	
	public saveInfo(String operation, int value){
		if (operation=="eq"){
			itemsNumberEqualsFilter=value
		}else if (operation=="neq"){
			itemsNumberNotEquals=value
		}else if (operation==EMPTY_FILTER){
			itemsNumberEmptyFilter=value
		}
	}
	
	public String toString(){
		if (isCorrectCounter()){
			"OK. All is correct. EmptyFilterResult = EqualsFilterResult + NotEqualsFilterResult [${itemsNumberEmptyFilter} = ${itemsNumberEqualsFilter} + ${itemsNumberNotEquals}]"	
		}else{
			"ERROR. EmptyFilterResult != EqualsFilterResult + NotEqualsFilterResult [${itemsNumberEmptyFilter} = ${itemsNumberEqualsFilter} + ${itemsNumberNotEquals}]"
		}
	}
}
