/*********************************************************************************
 *                                                                               *
 * The MIT License                                                               *
 *                                                                               *
 * Copyright (c) 2015-2020 aoju.org and other contributors.                      *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 ********************************************************************************/
package org.aoju.bus.core.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Iterator}对象转{@link Enumeration}.
 *
 * @param <E> 元素类型
 * @author Kimi Liu
 * @version 5.6.8
 * @since JDK 1.8+
 */
public class IteratorEnumeration<E> implements Enumeration<E> {

    private Iterator<? extends E> iterator;


    public IteratorEnumeration() {
    }

    /**
     * 构造
     *
     * @param iterator {@link Iterator}对象
     */
    public IteratorEnumeration(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    /**
     * 如果底层迭代器有更多的元素，则返回true
     *
     * @return 如果底层迭代器有更多的元素，则为true
     */
    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     * 从基础迭代器返回下一个元素.
     *
     * @return 底层迭代器的下一个元素.
     */
    @Override
    public E nextElement() {
        return iterator.next();
    }

    public Iterator<? extends E> getIterator() {
        return iterator;
    }

    public void setIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

}
