/**
 * Copyright 2011 Alexey Ragozin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gridkit.coherence.search.timeseries;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tangosol.util.aggregator.DistinctValues;
import com.tangosol.util.extractor.KeyExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AlwaysFilter;

/**
 * @author Alexey Ragozin (alexey.ragozin@gmail.com)
 */
public class SimpleAggregation_TestSet extends AbstractTimeseriesFunctional_TestSet {

	TimeSeriesHelper<String, SampleValue, Long> helper = new TimeSeriesHelper<String, SampleValue, Long>(
			new KeyExtractor("getSerieKey"), 
			new ReflectionExtractor("getTimestamp"));
	
	{
		helper.setAffinityEnabled(useAffinity);
	}
	
	@Before
	public void cleanUp() {
//		helper.destroyIndex(testCache);
		testCache.clear();
		helper.createIndex(testCache);
	}
	
	private void fixture1() {
		testCache.put(key("A", 1), version("A-10", 10));
		testCache.put(key("A", 2), version("A-12", 12));
		testCache.put(key("A", 3), version("A-20", 20));
		testCache.put(key("B", 1), version("B-8", 8));
		testCache.put(key("B", 2), version("B-11", 11));
		testCache.put(key("B", 3), version("B-13", 13));
		testCache.put(key("B", 4), version("B-21", 21));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void aggregatorTest() {
		fixture1();
		
		Collection<Object> result = (Collection<Object>) testCache.aggregate(AlwaysFilter.INSTANCE, new SurafceAggregator(10));
		TreeSet<Object> sq = new TreeSet<Object>(result);
		
		Assert.assertEquals(sq.toString(), "[A-10, B-8]");
		
		result = (Collection<Object>) testCache.aggregate(helper.floor(10l), new DistinctValues("getValue"));
		sq = new TreeSet<Object>(result);

		Assert.assertEquals(sq.toString(), "[A-10, B-8]");
	}
}
