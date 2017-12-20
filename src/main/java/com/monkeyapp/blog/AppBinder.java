/*
MIT License

Copyright (c) 2017 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.monkeyapp.blog;


import com.monkeyapp.blog.adapters.PaperAdapterImpl;
import com.monkeyapp.blog.models.PaperRepository;
import com.monkeyapp.blog.models.PaperRepositoryImpl;
import com.monkeyapp.blog.models.PostIdRepository;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class AppBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bindFactory(PostIdRepositoryFactory.class).to(PostIdRepository.class).in(Singleton.class);
        bindFactory(PaperRepositoryFactory.class).to(PaperRepository.class).in(Singleton.class);
    }

    private static class PaperRepositoryFactory implements Factory<PaperRepository> {
        @Override
        public PaperRepository provide() {
            return new PaperRepositoryImpl(AppContext.getPostIdRepository(),
                                           new PaperAdapterImpl()) ;
        }

        @Override
        public void dispose(PaperRepository instance) {
        }
    }

    private static class PostIdRepositoryFactory implements Factory<PostIdRepository> {
        @Override
        public PostIdRepository provide() {
            return AppContext.getPostIdRepository();
        }

        @Override
        public void dispose(PostIdRepository instance) {
        }
    }
}
