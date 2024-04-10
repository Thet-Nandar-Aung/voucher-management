package sg.edu.nus.iss.springboot.voucher.management.exception;

public class VoucherAppErrorException extends RuntimeException {

    public VoucherAppErrorException(String message) {
        super(message);
    }

}
