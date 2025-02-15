package tpgroup.view.cli.component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FormFieldFactory {
	private static FormFieldFactory instance;

	private FormFieldFactory(){
		super();
	}

	Map<String, FormFieldComp<?>> registry;

	public <T> FormFieldComp<T> newDefault(String prompt, Function<String, T> conversion){
		return new DefaultFormFieldComp<>(prompt, conversion);
	}

	public <T> FormFieldComp<T> newSelectItem(String prompt, List<T> items){
		return new SelectItemFormFieldComp<>(prompt, items);
	}

	public <T> FormFieldComp<T> newSelectItem(String prompt, List<T> items, boolean nullable){
		return new SelectItemFormFieldComp<>(prompt, items);
	}

	public <T> FormFieldComp<T> newSelectItem(List<T> items){
		return new SelectItemFormFieldComp<>(items);
	}

	public <T> FormFieldComp<T> newSelectItem(List<T> items, boolean nullable){
		return new SelectItemFormFieldComp<>(items, nullable);
	}

	public FormFieldComp<String> newPwdField(String prompt){
		return new PwdFormFieldComp(prompt);
	}

	public FormFieldComp<Boolean> newConfField(String prompt){
		return new ConfFormFieldComp(prompt);
	}

	public <T> FormFieldComp<List<T>> newMultiItem(String prompt, Function<String, T> conversion){
		return new MultipleItemsFormFieldComp<>(prompt, conversion);
	}

	@SuppressWarnings("unchecked")
	public <T> FormFieldComp<T> getRegistered(String key){
		return (FormFieldComp<T>) registry.get(key);
	}

	public static FormFieldFactory getInstance(){
		if(instance == null){
			instance = new FormFieldFactory();
		}
		return instance;
	}
	
}
