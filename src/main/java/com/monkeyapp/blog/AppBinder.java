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


import com.monkeyapp.blog.models.PostAdapter;
import com.monkeyapp.blog.models.PostAdapterImpl;
import com.monkeyapp.blog.models.PostRepository;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class AppBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(PostAdapterImpl.class).to(PostAdapter.class);
        bindFactory(PostRepositoryFactory.class).to(PostRepository.class).in(Singleton.class);
    }

    private static class PostRepositoryFactory implements Factory<PostRepository> {
        @Override
        public PostRepository provide() {
            return AppContext.getPostRepository();
        }

        @Override
        public void dispose(PostRepository instance) {

        }
    }
}
