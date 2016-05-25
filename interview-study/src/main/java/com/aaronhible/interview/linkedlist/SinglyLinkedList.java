package com.aaronhible.interview.linkedlist;

public class SinglyLinkedList<T> extends AbstractLinkedList<T> {
    @Override
    protected void setLink(final Element<T> previous, final Element<T> element) {
        previous.setNext(element);
    }
}
