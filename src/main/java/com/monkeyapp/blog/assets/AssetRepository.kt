/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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
package com.monkeyapp.blog.assets

class AssetRepository(private val postRegister: AssetFile,
                      private val pageRegister: AssetFile) {

    fun getPostList(tag: String = "",
                    offset: Int = 0,
                    maxPosts: Int = Int.MAX_VALUE): List<AssetFile> {

        return postRegister.childAssets
                .asSequence()
                .filter(AssetFile::isMarkdown)
                .filter { tag.isEmpty() ||  it.metadata.tag == tag }
                .sortedByDescending(AssetFile::priority)
                .drop(offset)
                .take(maxPosts)
                .toList()
    }

    fun getPost(year: String, monthday: String, title: String): AssetFile? {
        if (year.isEmpty() || monthday.isEmpty() || title.isEmpty()) {
            return null
        }

        return postRegister.childAssets
                .firstOrNull {
                    it.isMarkdown &&
                    year == it.metadata.year &&
                    monthday == it.metadata.monthday &&
                    title.equals(it.metadata.title, ignoreCase = true)
                }
    }

    fun getPage(title: String): AssetFile? {
        if (title.isEmpty()) {
            return null
        }

        return pageRegister.childAssets
                .firstOrNull {
                    it.isMarkdown &&
                    title.equals(it.metadata.title, ignoreCase = true)
                }
    }

    val tags: List<Pair<String, Int>>
        get() {
            return postRegister.childAssets
                    .map(AssetFile::metadata)
                    .groupBy(AssetFile.Metadata::tag)
                    .map{ it.key to it.value.size }
        }
}