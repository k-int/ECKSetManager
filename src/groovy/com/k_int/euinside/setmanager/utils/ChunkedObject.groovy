package com.k_int.euinside.setmanager.utils

class ChunkedObject {

	/** Object that needs to be chunked */
	private def object = null;
	
	/** The size of the object */ 
	private int objectSize = 0;
	
	/** The current offset within the object from where the next chunk will come from */
	private int offset = 0;
	
	/** The maximum size of a chunk */
	private int maxChunkSize = 0;

	/**
	 * Constructor that generates a null ChunkedObject
	 * 
	 */
	public ChunkedObject() {
	}	

	/**
	 * Constructor for a ChunkedObject
	 * 
	 * @param object The object that is to be chunked
	 * @param maxChunkSize The maximum size of each chunk
	 * 
	 */
	public ChunkedObject(object, int maxChunkSize) {
		this.object = object;
		this.maxChunkSize = maxChunkSize;
		if (object != null) {
			objectSize = object.size();
		}
	}	

	/**
	 * Is there any more of the object to be chunked
	 * 
	 * @return true if some of the object is still to be chunked, false if not
	 */
	public boolean hasMore() {
		return(offset < objectSize);
	}

	/**
	 * 	Gets hold of the next chunk of the object
	 * 
	 * @return The next chunk or null if we have reached the end of the object
	 */
	public def getNextChunk() {
		def chunk = null;
		if (hasMore()) {
			int rangeEnd = offset + maxChunkSize;
			if (rangeEnd > objectSize) {
				rangeEnd = objectSize;
			}
			chunk = object.getAt(new IntRange(offset, rangeEnd - 1));
			offset = rangeEnd;
		}
		return(chunk);  
	}
}
