package sg.edu.nus.iss.springboot.voucher.management.strategy;

public interface IAPIHelperValidationStrategy<T> {

	String validateCreation(T data) ;
	String validateUpdating(T data) ;
}
