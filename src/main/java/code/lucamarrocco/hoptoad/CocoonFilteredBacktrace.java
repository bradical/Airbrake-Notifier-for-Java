package code.lucamarrocco.hoptoad;


public class CocoonFilteredBacktrace extends Backtrace {

	public CocoonFilteredBacktrace(Iterable<String> backtrace) {
		super(backtrace);
		ignoreCocoon();
		ignoreMozilla();
		ignoreSpringSecurity();
		ignoreMortbayJetty();
		filter();
	}

}
