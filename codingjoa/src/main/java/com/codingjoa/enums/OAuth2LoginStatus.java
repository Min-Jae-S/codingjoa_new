package com.codingjoa.enums;

public enum OAuth2LoginStatus {
	
	NEW,		// user newly registered via OAuth2
	LINKED,		// existing user linked an OAuth2 account
	LOGGED_IN;	// user logged in with an already linked OAuth2 account
}
