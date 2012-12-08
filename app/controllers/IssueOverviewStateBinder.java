package controllers;

import play.mvc.PathBindable;

/**
 * Ein {@link IssueOverviewStateBinder} dient zum bind und unbind von
 * {@link IssuesOverviewState}s. Ein teil der angeforderten Pfade wird dabei in
 * jewiels den entsprechenden State umgewandelt, z.B. wird ein GET
 * /services/issues/open zu {@link IssueOverviewStateBinder#OPEN}.
 * 
 * Da Play 2 aktuell nur self recursive types erlaubt gibt eine Binder immer
 * erst sich als Wrapper um den eigentlichen state zurueck (nicht schoen aber
 * aktuell nicht wirklich umgehbar).
 * 
 */
public class IssueOverviewStateBinder implements PathBindable<IssueOverviewStateBinder> {

	public final static IssueOverviewStateBinder OPEN = new IssueOverviewStateBinder(IssuesOverviewState.OPEN);

	public final static IssueOverviewStateBinder CLOSED = new IssueOverviewStateBinder(IssuesOverviewState.CLOSED);

	public final static IssueOverviewStateBinder ASSIGNED_OTHERS = new IssueOverviewStateBinder(IssuesOverviewState.ASSIGNED_OTHERS);

	public final static IssueOverviewStateBinder ASSIGNED_CURRENT_USER = new IssueOverviewStateBinder(IssuesOverviewState.ASSIGNED_CURRENT_USER);

	public final static IssueOverviewStateBinder ALL = new IssueOverviewStateBinder(IssuesOverviewState.ALL);

	private IssuesOverviewState state;

	public IssueOverviewStateBinder() {
		super();
	}

	public IssueOverviewStateBinder(IssuesOverviewState state) {
		super();
		this.state = state;
	}

	@Override
	public String javascriptUnbind() {
		throw new UnsupportedOperationException("javascript unbind is not supported for this type!");
	}

	@Override
	public String unbind(String arg0) {
		return state.unbind();
	}

	@Override
	public IssueOverviewStateBinder bind(String arg0, String matchedPath) {

		for (IssuesOverviewState state : IssuesOverviewState.values()) {
			if (matchedPath.matches(state.getRegex())) {
				return new IssueOverviewStateBinder(state);
			}
		}
		throw new IllegalStateException("unknown state!");
	}

	public IssuesOverviewState getState() {
		return state;
	}
	
	public static IssueOverviewStateBinder create(IssuesOverviewState state) {
		
		switch(state) {
		case OPEN: 
			return IssueOverviewStateBinder.OPEN;
		case ALL:
			return IssueOverviewStateBinder.ALL;
		case ASSIGNED_CURRENT_USER:
			return IssueOverviewStateBinder.ASSIGNED_CURRENT_USER;
		case ASSIGNED_OTHERS:
			return IssueOverviewStateBinder.ASSIGNED_OTHERS;
		case CLOSED:
			return IssueOverviewStateBinder.CLOSED;
		default:
			throw new IllegalStateException("unknown state!");
		}
	}

}
