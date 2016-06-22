/*
 * Copyright (c) 2012, 2016, Sopra Banking Software and/or its affiliates.
 * All rights reserved.
 * SOPRA BANKING SOFTWARE PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 *
 * http://forge.corp.sopra/confluence/display/EVOTA/EvoTA+License
 */
package experiment;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Maps.*;
import static com.sopra.framework.commons.searchcriteria.PropertySelector.*;
import static org.apache.commons.lang.StringUtils.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.sopra.framework.commons.dataaccess.GenericDaoQualifier;
import com.sopra.framework.commons.searchcriteria.OrderBy;
import com.sopra.framework.commons.searchcriteria.OrderByDirection;
import com.sopra.framework.commons.searchcriteria.PropertySelector;
import com.sopra.framework.commons.searchcriteria.Range;
import com.sopra.framework.commons.searchcriteria.SearchMode;

/**
 * The SearchParameters is used to pass search parameters to the DAO layer. Its usage keeps 'find' method signatures in
 * the DAO/Service layer simple. A SearchParameters helps you drive your search in the following areas:
 * <ul>
 * <li>Configure the search mode (EQUALS, LIKE, ...)</li>
 * <li>Pagination: it allows you to limit your search results to a specific range.</li>
 * <li>Allow you to specify ORDER BY and ASC/DESC</li>
 * <li>Enable/disable case sensitivity</li>
 * <li>Enable/disable 2d level cache</li>
 * <li>LIKE search against all string values: simply set the searchPattern property</li>
 * <li>Named query: if you set a named query it will be executed. Named queries can be defined in annotation or
 * src/main/resources/META-INF/orm.xml</li>
 * <li>FullTextSearch: simply set the term property (requires Hibernate Search)</li>
 * </ul>
 * Note : All requests are limited to a maximum number of elements to prevent resource exhaustion.
 * @see GenericDaoQualifier
 * @see SearchMode
 * @see OrderBy
 * @see Range
 * @see NamedQueryUtil
 * @see PropertySelector
 * @see EntitySelector
 */
public class ROASTER106
    implements Serializable
{
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The search mode.
     */
    private SearchMode searchMode = SearchMode.EQUALS;

    /**
     * The and mode.
     */
    private boolean andMode = true;

    /**
     * named query related.
     */
    private String namedQuery;

    /**
     * A map with String keys and Object values for parameters.
     */
    private Map<String, Object> parameters = newHashMap();

    /**
     * A list of orders.
     */
    private List<OrderBy> orders = newArrayList();

    /**
     * technical parameters.
     */
    private boolean caseSensitive = true;

    /**
     * pagination.
     */
    private int maxResults = -1;

    /**
     * The first page.
     */
    private int first = 0;

    /**
     * The size of a page.
     */
    private int pageSize = 0;

    /**
     * fetches.
     */
    private List<HashMap<String, Class<?>>> fetches = newArrayList();

    /**
     * ranges.
     */
    private List<Range<?, ?>> ranges = newArrayList();

    /**
     * property selectors.
     */
    private List<PropertySelector<?, ?>> properties = newArrayList();

    /**
     * pattern to match against all strings.
     */
    private String searchPattern;

    /**
     * Warn: before enabling cache for queries, check this: https://hibernate.atlassian.net/browse/HHH-1523.
     */
    private Boolean cacheable = Boolean.FALSE;

    /**
     * The cache region.
     */
    private String cacheRegion;

    /**
     * extra parameters.
     */
    private Map<String, Object> extraParameters = newHashMap();

    /**
     * use and in X to many.
     */
    private boolean useAndInXToMany = true;

    /**
     * use distinct.
     */
    private boolean useDistinct = false;

    // -----------------------------------
    // SearchMode
    // -----------------------------------

    /**
     * Fluently set the @{link SearchMode}. It defaults to EQUALS.
     * @param searchMode the new search mode
     * @see SearchMode#EQUALS
     */
    /**
     * Fluently set the @{link SearchMode}. It defaults to EQUALS.
     * @see SearchMode#EQUALS
     * @param searchMode search mode
     */
    public void setSearchMode(SearchMode searchMode)
    {
        this.searchMode = checkNotNull(searchMode);
    }

    /**
     * Return the @{link SearchMode}. It defaults to EQUALS.
     * @return search mode
     * @see SearchMode#EQUALS
     */
    public SearchMode getSearchMode()
    {
        return searchMode;
    }

    /**
     * Test the equality of search mode.
     * @param searchMode search mode
     * @return State of the equality
     */
    public boolean is(SearchMode searchMode)
    {
        return getSearchMode() == searchMode;
    }

    /**
     * Fluently set the @{link SearchMode}. It defaults to EQUALS.
     * @param searchMode search mode
     * @return <code>this</code>
     * @see SearchMode#EQUALS
     */
    public SearchParameter searchMode(SearchMode searchMode)
    {
        setSearchMode(searchMode);
        return this;
    }

    /**
     * Use the EQUALS @{link SearchMode}.
     * @return value of search parameter for equals search mode
     * @see SearchMode#EQUALS
     */
    public SearchParameter equals()
    {
        return searchMode(SearchMode.EQUALS);
    }

    /**
     * Use the ANYWHERE @{link SearchMode}.
     * @return value of search parameter for anywhere search mode
     * @see SearchMode#ANYWHERE
     */
    public SearchParameter anywhere()
    {
        return searchMode(SearchMode.ANYWHERE);
    }

    /**
     * Use the STARTING_LIKE @{link SearchMode}.
     * @return value of search parameter for starting like search mode
     * @see SearchMode#STARTING_LIKE
     */
    public SearchParameter startingLike()
    {
        return searchMode(SearchMode.STARTING_LIKE);
    }

    /**
     * Use the LIKE @{link SearchMode}.
     * @return value of search parameter for like search mode
     * @see SearchMode#LIKE
     */
    public SearchParameter like()
    {
        return searchMode(SearchMode.LIKE);
    }

    /**
     * Use the ENDING_LIKE @{link SearchMode}.
     * @return value of search parameter for ending like search mode
     * @see SearchMode#ENDING_LIKE
     */
    public SearchParameter endingLike()
    {
        return searchMode(SearchMode.ENDING_LIKE);
    }

    // -----------------------------------
    // Predicate mode
    // -----------------------------------

    /**
     * Set andMode.
     * @param andMode andMode
     */
    public void setAndMode(boolean andMode)
    {
        this.andMode = andMode;
    }

    /**
     * Get andMode.
     * @return andMode
     */
    public boolean isAndMode()
    {
        return andMode;
    }

    /**
     * use <code>and</code> to build the final predicate.
     * @return <code>this</code>
     */
    public SearchParameter andMode()
    {
        setAndMode(true);
        return this;
    }

    /**
     * use <code>or</code> to build the final predicate.
     * @return <code>this</code>
     */
    public SearchParameter orMode()
    {
        setAndMode(false);
        return this;
    }

    // -----------------------------------
    // Named query support
    // -----------------------------------

    /**
     * Returns true if a named query has been set, false otherwise. When it returns true, the DAO layer will call the
     * namedQuery.
     * @return true if the String is not empty and not null and not whitespace
     */
    public boolean hasNamedQuery()
    {
        return isNotBlank(namedQuery);
    }

    /**
     * Set the named query to be used by the DAO layer. Null by default.
     * @param namedQuery named query
     */
    public void setNamedQuery(String namedQuery)
    {
        this.namedQuery = namedQuery;
    }

    /**
     * Return the name of the named query to be used by the DAO layer.
     * @return named query
     */
    public String getNamedQuery()
    {
        return namedQuery;
    }

    /**
     * Set the parameters for the named query.
     * @param parameters a map of parameters
     */
    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = checkNotNull(parameters);
    }

    /**
     * Set the parameters for the named query.
     * @param name parameter key
     * @param value parameter value
     */
    public void addNamedQueryParameter(String name, Object value)
    {
        parameters.put(checkNotNull(name), checkNotNull(value));
    }

    /**
     * The parameters associated with the named query, if any.
     * @return parameters
     */
    public Map<String, Object> getParameters()
    {
        return parameters;
    }

    /**
     * Return the value of the given parameter name.
     * @param parameterName parameter name
     * @return parameter value
     */
    public Object getNamedQueryParameter(String parameterName)
    {
        return parameters.get(checkNotNull(parameterName));
    }

    /**
     * Fluently set the named query to be used by the DAO layer. Null by default.
     * @param nameQuery name query
     * @return <code>this</code>
     */
    public SearchParameter namedQuery(String nameQuery)
    {
        setNamedQuery(nameQuery);
        return this;
    }

    /**
     * Fluently set the parameters for the named query.
     * @param parameters parameter map
     * @return <code>this</code>
     */
    public SearchParameter namedQueryParameters(Map<String, Object> parameters)
    {
        setParameters(parameters);
        return this;
    }

    /**
     * Fluently set the parameter for the named query.
     * @param name parameter name
     * @param value parameter value
     * @return <code>this</code>
     */
    public SearchParameter namedQueryParameter(String name, Object value)
    {
        addNamedQueryParameter(name, value);
        return this;
    }

    // -----------------------------------
    // Search pattern support
    // -----------------------------------

    /**
     * When it returns true, it indicates to the DAO layer to use the given searchPattern on all string properties.
     * @return true if the String is not empty and not null and not whitespace
     */
    public boolean hasSearchPattern()
    {
        return isNotBlank(searchPattern);
    }

    /**
     * Set the pattern which may contains wildcards (ex: <code>e%r%ka</code> ).
     * <p>
     * The given searchPattern is used by the DAO layer on all string properties. Null by default.
     * @param searchPattern String who represent a string pattern
     */
    public void setSearchPattern(String searchPattern)
    {
        this.searchPattern = searchPattern;
    }

    /**
     * Fluently set the pattern which may contains wildcards.
     * @param searchPattern search pattern
     * @return <code>this</code>
     */
    public SearchParameter searchPattern(String searchPattern)
    {
        setSearchPattern(searchPattern);
        return this;
    }

    /**
     * Get search pattern.
     * @return search pattern
     */
    public String getSearchPattern()
    {
        return searchPattern;
    }

    // -----------------------------------
    // Case sensitiveness support
    // -----------------------------------

    /**
     * Set the case sensitiveness. Defaults to false.
     * @param caseSensitive case sensitive
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Get case sensitive.
     * @return case sensitive
     */
    public boolean isCaseSensitive()
    {
        return caseSensitive;
    }

    /**
     * Get case insensitive.
     * @return case insensitive
     */
    public boolean isCaseInsensitive()
    {
        return !caseSensitive;
    }

    /**
     * Fluently set the case sensitiveness. Defaults to false.
     * @param caseSensitive case sensitive
     * @return <code>this</code>
     */
    public SearchParameter caseSensitive(boolean caseSensitive)
    {
        setCaseSensitive(caseSensitive);
        return this;
    }

    /**
     * Fluently set the case sensitiveness to true.
     * @return <code>this</code> with case sensitive set to <code>true</code>
     */
    public SearchParameter caseSensitive()
    {
        return caseSensitive(true);
    }

    /**
     * Fluently set the case sensitiveness to false.
     * @return <code>this</code> with case sensitive set to <code>true</code>
     */
    public SearchParameter caseInsensitive()
    {
        return caseSensitive(false);
    }

    // -----------------------------------
    // Order by support
    // -----------------------------------

    /**
     * Get orders.
     * @return orders
     */
    public List<OrderBy> getOrders()
    {
        return orders;
    }

    /**
     * Add an orderBy to the orderBy list.
     * @param orderBy orderBy to add
     */
    public void addOrderBy(OrderBy orderBy)
    {
        orders.add(checkNotNull(orderBy));
    }

    /**
     * Check if orderBy list isn't empty.
     * @return <code>true</code> if orders isn't empty
     */
    public boolean hasOrders()
    {
        return !orders.isEmpty();
    }

    /**
     * Add all not null orderBy to ordereBy list.
     * @param orderBys dynamic sized tab of orderBy
     * @return <code>this</code>
     */
    public SearchParameter orderBy(OrderBy... orderBys)
    {
        for (OrderBy orderBy : checkNotNull(orderBys))
        {
            addOrderBy(orderBy);
        }
        return this;
    }

    /**
     * Set the orderBy to ascending for all attributes.
     * @param attributes list of attributes
     * @return <code>this</code>
     */
    public SearchParameter asc(HashMap<String, Class<?>> attributes)
    {
        return orderBy(new OrderBy(OrderByDirection.ASC, attributes));
    }

    /**
     * Set the orderBy to descending for all attributes.
     * @param attributes list of attributes
     * @return <code>this</code>
     */
    public SearchParameter desc(HashMap<String, Class<?>> attributes)
    {
        return orderBy(new OrderBy(OrderByDirection.DESC, attributes));
    }

    /**
     * Set the orderBy to parameter orderbydirection for all attributes.
     * @param orderByDirection order by direction
     * @param attributes list of attributes
     * @return <code>this</code>
     */
    public SearchParameter orderBy(OrderByDirection orderByDirection, HashMap<String, Class<?>> attributes)
    {
        return orderBy(new OrderBy(orderByDirection, attributes));
    }

    /**
     * Set the orderBy to ascending for property.
     * @param property property
     * @param from from
     * @return <code>this</code>
     */
    public SearchParameter asc(String property, Class<?> from)
    {
        return orderBy(new OrderBy(OrderByDirection.ASC, property, from));
    }

    /**
     * Set the orderBy to descending for property.
     * @param property property
     * @param from from
     * @return <code>this</code>
     */
    public SearchParameter desc(String property, Class<?> from)
    {
        return orderBy(new OrderBy(OrderByDirection.DESC, property, from));
    }

    /**
     * Set the orderBy to ascending for property.
     * @param orderByDirection order by direction
     * @param property property
     * @param from from
     * @return <code>this</code>
     */
    public SearchParameter orderBy(OrderByDirection orderByDirection, String property, Class<?> from)
    {
        return orderBy(new OrderBy(orderByDirection, property, from));
    }

    // -----------------------------------
    // Search by range support
    // -----------------------------------

    /**
     * Get ranges.
     * @return ranges
     */
    @XmlTransient
    public List<Range<?, ?>> getRanges()
    {
        return ranges;
    }

    /**
     * Add a range (not null) to range list.
     * @param range range element to add
     */
    public void addRange(Range<?, ?> range)
    {
        ranges.add(checkNotNull(range));
    }

    /**
     * Check if range list isn't empty.
     * @return <code>true</code> if ranges isn't empty
     */
    public boolean hasRanges()
    {
        return !ranges.isEmpty();
    }

    /**
     * Add all not null range to range list.
     * @param ranges dynamic sized tab of range
     * @return <code>this</code>
     */
    public SearchParameter range(Range<?, ?>... ranges)
    {
        for (Range<?, ?> range : checkNotNull(ranges))
        {
            addRange(range);
        }
        return this;
    }

    /**
     * Create a new range that begins by <code>from</code> and end by <code>to</code>.
     * @param <E> Generic parameter
     * @param <D> Generic parameter
     * @param from the lower boundary of this range. Null means no lower boundary.
     * @param to the upper boundary of this range. Null means no upper boundary.
     * @param attributes the path to the attribute of an existing entity.
     * @return <code>this</code>
     */
    public <E, D extends Comparable<? super D>> SearchParameter range(D from, D to,
        HashMap<String, Class<?>> attributes)
    {
        return range(new Range<E, D>(from, to, attributes));
    }

    // -----------------------------------
    // Search by property selector support
    // -----------------------------------

    /**
     * Get properties.
     * @return properties
     */
    public List<PropertySelector<?, ?>> getProperties()
    {
        return properties;
    }

    /**
     * Add a not null property to the property list.
     * @param propertySelector property selector
     */
    public void addProperty(PropertySelector<?, ?> propertySelector)
    {
        properties.add(checkNotNull(propertySelector));
    }

    /**
     * Check if properties isn't empty.
     * @return <code>true</code> if properties isn't empty
     */
    public boolean hasProperties()
    {
        return !properties.isEmpty();
    }

    /**
     * Add not null property to the property list.
     * @param propertySelectors dynamic tab of property
     * @return <code>this</code>
     */
    public SearchParameter property(PropertySelector<?, ?>... propertySelectors)
    {
        for (PropertySelector<?, ?> propertySelector : checkNotNull(propertySelectors))
        {
            addProperty(propertySelector);
        }
        return this;
    }

    /**
     * Add not null property to the property list.
     * @param <F> Generic parameter
     * @param fields map of fields
     * @param selected dynamic tab of selector
     * @return <code>this</code>
     */
    public <F> SearchParameter property(HashMap<String, Class<?>> fields, F... selected)
    {
        return property(newPropertySelector(fields).selected(selected));
    }

    // -----------------------------------
    // Pagination support
    // -----------------------------------

    /**
     * Set the maximum number of results to retrieve. Pass -1 for no limits.
     * @param maxResults max results
     */
    public void setMaxResults(int maxResults)
    {
        this.maxResults = maxResults;
    }

    /**
     * Get max results.
     * @return max results
     */
    public int getMaxResults()
    {
        return maxResults;
    }

    /**
     * Set the position of the first result to retrieve.
     * @param first position of the first result, numbered from 0
     */
    public void setFirst(int first)
    {
        this.first = first;
    }

    /**
     * Get first.
     * @return first
     */
    public int getFirst()
    {
        return first;
    }

    /**
     * Set the page size, that is the maximum number of result to retrieve.
     * @param pageSize page size
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * Get page size.
     * @return page size
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * Set max results.
     * @param maxResults max results
     * @return <code>this</code>
     */
    public SearchParameter maxResults(int maxResults)
    {
        setMaxResults(maxResults);
        return this;
    }

    /**
     * Set max result to -1, that means no limit.
     * @return <code>this</code>
     */
    public SearchParameter noLimit()
    {
        setMaxResults(-1);
        return this;
    }

    /**
     * Set max result to 500, that means broad search limit.
     * @return <code>this</code>
     */
    public SearchParameter limitBroadSearch()
    {
        setMaxResults(500);
        return this;
    }

    /**
     * Set first.
     * @param first first
     * @return <code>this</code>
     */
    public SearchParameter first(int first)
    {
        setFirst(first);
        return this;
    }

    /**
     * Set page size.
     * @param pageSize page size
     * @return <code>this</code>
     */
    public SearchParameter pageSize(int pageSize)
    {
        setPageSize(pageSize);
        return this;
    }

    // -----------------------------------------
    // Fetch associated entity using a LEFT Join
    // -----------------------------------------

    /**
     * Returns the attributes (x-to-one association) which must be fetched with a left join.
     * @return fetches
     */
    public List<HashMap<String, Class<?>>> getFetches()
    {
        return fetches;
    }

    /**
     * Check if fetches ins't empty.
     * @return <code>true</code> if fetches isn't empty
     */
    public boolean hasFetches()
    {
        return !fetches.isEmpty();
    }

    /**
     * The given attribute (x-to-one association) will be fetched with a left join.
     * @param attributes map of attributes
     */
    public void addFetch(HashMap<String, Class<?>> attributes)
    {
        fetches.add(checkNotNull(attributes));
    }

    /**
     * Fluently set the fetch attribute.
     * @param attributes map of attributes
     * @return <code>this</code>
     */
    public SearchParameter fetch(HashMap<String, Class<?>> attributes)
    {
        addFetch(attributes);
        return this;
    }

    // -----------------------------------
    // Caching support
    // -----------------------------------

    /**
     * Default to false. Please read https://hibernate.atlassian.net/browse/HHH-1523 before using cache.
     * @param cacheable cacheable
     */
    public void setCacheable(boolean cacheable)
    {
        this.cacheable = cacheable;
    }

    /**
     * Get cacheable.
     * @return cacheable
     */
    public boolean isCacheable()
    {
        return cacheable;
    }

    /**
     * Check if cacheRegion isn't empty.
     * @return <code>true</code> if the String is not empty and not null and not whitespace
     */
    public boolean hasCacheRegion()
    {
        return isNotBlank(cacheRegion);
    }

    /**
     * Set cache region.
     * @param cacheRegion cache region
     */
    public void setCacheRegion(String cacheRegion)
    {
        this.cacheRegion = cacheRegion;
    }

    /**
     * Get cache region.
     * @return cache region
     */
    public String getCacheRegion()
    {
        return cacheRegion;
    }

    /**
     * Set cacheable.
     * @param cacheable cacheable
     * @return <code>this</code>
     */
    public SearchParameter cacheable(boolean cacheable)
    {
        setCacheable(cacheable);
        return this;
    }

    /**
     * Set cacheable to true, that means cache is enabled.
     * @return <code>this</code>
     */
    public SearchParameter enableCache()
    {
        setCacheable(true);
        return this;
    }

    /**
     * Set cacheable to false, that means cache is disabled.
     * @return <code>this</code>
     */
    public SearchParameter disableCache()
    {
        setCacheable(false);
        return this;
    }

    /**
     * Set cache region.
     * @param cacheRegion cache region (not null)
     * @return <code>this</code>
     */
    public SearchParameter cacheRegion(String cacheRegion)
    {
        setCacheRegion(checkNotNull(cacheRegion));
        return this;
    }

    // -----------------------------------
    // Extra parameters
    // -----------------------------------

    /**
     * Set additionnal parameters.
     * @param extraParameters a map of extra parameters
     */
    public void setExtraParameters(Map<String, Object> extraParameters)
    {
        this.extraParameters = extraParameters;
    }

    /**
     * Get extra parameters.
     * @return extra parameters
     */
    public Map<String, Object> getExtraParameters()
    {
        return extraParameters;
    }

    /**
     * Add additionnal parameter.
     * @param key key (not null)
     * @param object value
     * @return <code>this</code>
     */
    public SearchParameter addExtraParameter(String key, Object object)
    {
        extraParameters.put(checkNotNull(key), object);
        return this;
    }

    /**
     * Gets the extra parameter.
     * @param <T> the generic type
     * @param key the key
     * @return the extra parameter
     */
    /**
     * get additionnal parameter.
     * @param <T> Generic parameter
     * @param key key
     * @return <code>this</code>
     */
    @SuppressWarnings("unchecked")
    public <T> T getExtraParameter(String key)
    {
        return (T) extraParameters.get(key);
    }

    // -----------------------------------
    // Use and in XToMany Search
    // -----------------------------------

    /**
     * Set use and in X to many.
     * @param useAndInXToMany use and in X to many
     */
    public void setUseAndInXToMany(boolean useAndInXToMany)
    {
        this.useAndInXToMany = useAndInXToMany;
    }

    /**
     * Get use and in X to many.
     * @return use and in X to many
     */
    public boolean getUseAndInXToMany()
    {
        return useAndInXToMany;
    }

    /**
     * Set use and in X to many to false, that means use Or.
     * @return <code>this</code>
     */
    public SearchParameter useOrInXToMany()
    {
        return useAndInXToMany(false);
    }

    /**
     * Set use and in X to many to true, that means use And.
     * @return <code>this</code>
     */
    public SearchParameter useAndInXToMany()
    {
        return useAndInXToMany(true);
    }

    /**
     * Set use and in X to many.
     * @param xToManyAndMode x to many and mode
     * @return <code>this</code>
     */
    public SearchParameter useAndInXToMany(boolean xToManyAndMode)
    {
        setUseAndInXToMany(xToManyAndMode);
        return this;
    }

    // -----------------------------------
    // Distinct
    // -----------------------------------

    /**
     * Set distinct.
     * @param useDistinct use distinct
     */
    public void setDistinct(boolean useDistinct)
    {
        this.useDistinct = useDistinct;
    }

    /**
     * Get distinct.
     * @return distinct
     */
    public boolean getDistinct()
    {
        return useDistinct;
    }

    /**
     * Set distinct.
     * @param useDistinct use distinct
     * @return <code>this</code>
     */
    public SearchParameter distinct(boolean useDistinct)
    {
        setDistinct(useDistinct);
        return this;
    }

    /**
     * Set distinct to <code>true</code>.
     * @return <code>this</code>
     */
    public SearchParameter distinct()
    {
        return distinct(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
