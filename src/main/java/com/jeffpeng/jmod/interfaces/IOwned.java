package com.jeffpeng.jmod.interfaces;

import com.jeffpeng.jmod.JMODRepresentation;

public interface IOwned {
	public JMODRepresentation getOwner();
	public boolean hasOwner();
}
