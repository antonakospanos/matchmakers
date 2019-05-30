package com.workable.matchmakers.web.dto;

import java.io.Serializable;

public interface Dto<E extends Object> extends Serializable {

	public Dto<E> fromEntity(E entity);

	public E toEntity();

	public E toEntity(E entity);

}
