class Route {

	String forwardingRouter;

	int latency;

	public Route(String forwardingRouterIdentifier, int latency) {
		this.forwardingRouter = forwardingRouterIdentifier;
		this.latency = latency;
	}
}