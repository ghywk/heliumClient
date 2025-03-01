package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import java.lang.reflect.Array;
import java.util.*;

public class Cartesian {
    public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable<? extends Iterable<? extends T>> sets) {
        return new Cartesian.Product(clazz, toArray(Iterable.class, sets));
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> sets) {
        return arraysAsLists(cartesianProduct(Object.class, sets));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> arrays) {
        return Iterables.transform(arrays, new Cartesian.GetList());
    }

    private static <T> T[] toArray(Class<? super T> clazz, Iterable<? extends T> it) {
        List<T> list = Lists.newArrayList();

        for (T t : it) {
            list.add(t);
        }

        return list.toArray(createArray(clazz, list.size()));
    }

    private static <T> T[] createArray(Class<? super T> p_179319_0_, int p_179319_1_) {
        return (T[]) Array.newInstance(p_179319_0_, p_179319_1_);
    }

    static class GetList<T> implements Function<Object[], List<T>> {
        private GetList() {
        }

        public List<T> apply(Object[] p_apply_1_) {
            return Arrays.asList((T[]) p_apply_1_);
        }
    }

    static class Product<T> implements Iterable<T[]> {
        private final Class<T> clazz;
        private final Iterable<? extends T>[] iterables;

        private Product(Class<T> clazz, Iterable<? extends T>[] iterables) {
            this.clazz = clazz;
            this.iterables = iterables;
        }

        /**
         * <b><i> 改动说明：</b></i> <p>
         * <b>移除强制转换： </b><p>
         * 原代码：{@code Collections.singletonList((Object[]) Cartesian.createArray(...))} <p>
         * 新代码：{@code Collections.<T[]>singletonList(Cartesian.createArray(...))} <p>
         * 在JDK21中泛型类型推断更严格，通过Collections.<T[]>singletonList显式声明集合的泛型类型为T[]，替代原先的Object[]强制转换，避免未经检查的类型转换警告 <p>
         * <b>添加钻石运算符： </b><p>
         * 原代码：{@code new Cartesian.Product.ProductIterator(...)} <p>
         * 新代码：{@code  new Cartesian.Product.ProductIterator<>(...)} <p>
         * 明确指示编译器自动推断泛型类型（假设ProductIterator是泛型类），确保类型安全并适配JDK21的类型检查规则 <p>
         * <b>移除外层类型转换： </b><p>
         * 原代码：{@code  return (Iterator<T[]>) (...)} <p>
         * 新代码：{@code  return (...)} <p>
         * 由于两个分支现在都直接返回Iterator<T[]>类型（空数组分支通过显式泛型声明保证类型，迭代器分支通过钻石运算符保证类型），不再需要外层的类型强制转换 <p>
         *
         * 假设Cartesian.createArray(clazz, 0)返回的是T[]类型，这与显式声明的Collections.<T[]>singletonList(...)类型一致 <p>
         * 如果实际使用中仍有类型警告，可以在方法签名添加@SuppressWarnings("unchecked")注解（但需确保逻辑类型安全） <p>
         * 这种修改方式同时保持了对JDK8的兼容性，属于向前兼容的改进方案
         */
        public Iterator<T[]> iterator() {
            return (this.iterables.length <= 0)
                    // 创建空数组迭代器：JDK21 加强泛型类型检查，需要显式转换为正确的泛型数组类型
                    ? Collections.<T[]>singletonList(Cartesian.createArray(this.clazz, 0)).iterator()
                    // 使用钻石运算符保持类型安全（假设ProductIterator是泛型类）
                    : new Cartesian.Product.ProductIterator<>(this.clazz, this.iterables);
        }

        static class ProductIterator<T> extends UnmodifiableIterator<T[]> {
            private int index;
            private final Iterable<? extends T>[] iterables;
            private final Iterator<? extends T>[] iterators;
            private final T[] results;

            private ProductIterator(Class<T> clazz, Iterable<? extends T>[] iterables) {
                this.index = -2;
                this.iterables = iterables;
                this.iterators = Cartesian.createArray(Iterator.class, this.iterables.length);

                for (int i = 0; i < this.iterables.length; ++i) {
                    this.iterators[i] = iterables[i].iterator();
                }

                this.results = Cartesian.createArray(clazz, this.iterators.length);
            }

            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, null);
                Arrays.fill(this.results, null);
            }

            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;

                    for (Iterator<? extends T> iterator1 : this.iterators) {
                        if (!iterator1.hasNext()) {
                            this.endOfData();
                            break;
                        }
                    }

                    return true;
                } else {
                    if (this.index >= this.iterators.length) {
                        for (this.index = this.iterators.length - 1; this.index >= 0; --this.index) {
                            Iterator<? extends T> iterator = this.iterators[this.index];

                            if (iterator.hasNext()) {
                                break;
                            }

                            if (this.index == 0) {
                                this.endOfData();
                                break;
                            }

                            iterator = this.iterables[this.index].iterator();
                            this.iterators[this.index] = iterator;

                            if (!iterator.hasNext()) {
                                this.endOfData();
                                break;
                            }
                        }
                    }

                    return this.index >= 0;
                }
            }

            public T[] next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    while (this.index < this.iterators.length) {
                        this.results[this.index] = this.iterators[this.index].next();
                        ++this.index;
                    }

                    return this.results.clone();
                }
            }
        }
    }
}
