package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.google.common.collect.Maps;
import com.ncov.module.controller.resp.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * service基类，使用实体必须存在对应继承自baseMapper的原始mapper，并且可以使用
 *
 * @author hbh
 */
@Slf4j
public abstract class AbstractService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    /**
     * 查询count
     *
     * @param cls     表实体class
     * @param wrapper 条件构造器
     * @param <R>     表实体类型
     * @return count
     */
    public <R> int selectCount(Class<R> cls, Wrapper<R> wrapper) {
        return null == cls ? 0 : executeAndThen(cls
                , sqlSession -> sqlSession.<Integer>selectOne(sqlStatement(cls, SqlMethod.SELECT_COUNT)
                        , wrapper == null ? null : Collections.singletonMap(Constants.WRAPPER, wrapper))
                , SqlHelper::retCount);
    }

    /**
     * 根据id查询一条记录
     *
     * @param cls 表实体class
     * @param id  id
     * @param <R> 表实体类型
     * @return 实体对象或者null
     */
    public <R> R selectById(Class<R> cls, Serializable id) {
        return null == cls || null == id ?
                null : execute(cls, sqlSession -> sqlSession.selectOne(sqlStatement(cls, SqlMethod.SELECT_BY_ID), id));
    }

    /**
     * 保存一个实体对象到对应的表中
     *
     * @param o 实体对象
     * @return 是否操作成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(Object o) {
        Class<?> rClass;
        return null != o && executeAndThen(rClass = o.getClass()
                , sqlSession -> sqlSession.insert(sqlStatement(rClass, SqlMethod.INSERT_ONE), o)
                , this::retBool);
    }

    /**
     * 使用id更新给定实体对应表中的记录
     *
     * @param o 实体对象
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithId(Object o) {
        Class<?> rClass;
        return o != null && executeAndThen(rClass = o.getClass()
                , sqlSession -> sqlSession.update(sqlStatement(rClass, SqlMethod.UPDATE_BY_ID), Collections.singletonMap(Constants.ENTITY, o))
                , this::retBool);
    }

    /**
     * 使用条件构造器更新给定实体对应表中的记录
     *
     * @param cls           表实体class
     * @param updateWrapper 更新条件构造器
     * @return 是否更新成功
     */
    @Transactional(rollbackFor = Exception.class)
    public <R> boolean updateEntity(Class<R> cls, Wrapper<R> updateWrapper) {
        return null != cls && null != updateWrapper && executeAndThen(cls
                , sqlSession -> sqlSession.update(sqlStatement(cls, SqlMethod.UPDATE)
                        , Collections.singletonMap(Constants.WRAPPER, updateWrapper))
                , this::retBool);
    }

    /**
     * 查询给定实体类型和条件构造器在表中的集合
     *
     * @param cls     表实体class
     * @param wrapper 查询条件构造器
     * @return 记录集合
     */
    public <R> List<R> selectList(Class<R> cls, Wrapper<R> wrapper) {
        return cls == null ? Collections.emptyList() : execute(cls
                , sqlSession -> sqlSession.selectList(sqlStatement(cls, SqlMethod.SELECT_LIST)
                        , wrapper == null ? null : Collections.singletonMap(Constants.WRAPPER, wrapper)));
    }

    /**
     * 查询给定实体类型在对应表中所有的记录
     *
     * @param cls 表实体class
     * @param <R> 表实体类型
     * @return 记录集合
     */
    public <R> List<R> selectAll(Class<R> cls) {
        return selectList(cls, null);
    }

    /**
     * 根据给定实体类型和id删除对应表中的记录
     *
     * @param cls 表实体class
     * @param id  id
     * @param <R> 表实体类型
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public <R> boolean deleteById(Class<R> cls, Serializable id) {
        return null != cls && null != id && executeAndThen(cls
                , sqlSession -> sqlSession.delete(sqlStatement(cls, SqlMethod.DELETE_BY_ID), id)
                , this::retBool);
    }

    /**
     * 根据给定实体类型和条件构造器删除表中的记录
     *
     * @param cls     表实体class
     * @param wrapper 条件构造器
     * @param <R>     表实体类型
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public <R> boolean delete(Class<R> cls, Wrapper<R> wrapper) {
        return cls != null && executeAndThen(cls
                , sqlSession -> sqlSession.delete(sqlStatement(cls, SqlMethod.DELETE)
                        , wrapper == null ? null : Collections.singletonMap(Constants.WRAPPER, wrapper))
                , this::retBool);
    }

    /**
     * 分页查询，并将分页结果类型通过carryFunction转为目标类型
     *
     * @param clazz         表实体class
     * @param page          页数
     * @param size          数据条数
     * @param wrapper       条件构造器，可为空
     * @param carryFunction 类型转换函数
     * @param <L>           源类型
     * @param <R>           目标类型
     * @return 分页对象
     */
    public <L, R> Page<R> selectPage(Class<L> clazz
            , Integer page
            , Integer size
            , Wrapper<L> wrapper
            , Function<? super L, R> carryFunction) {
        Objects.requireNonNull(carryFunction, "carryFunction must be not null!");
        IPage<L> mpPage = page(clazz, page, size, wrapper);
        return Page.<R>builder()
                .page(page)
                .pageSize(size)
                .data(mpPage.getRecords().stream()
                        .filter(Objects::nonNull)
                        .map(carryFunction)
                        .collect(Collectors.toCollection(LinkedList::new)))
                .build();
    }

    /**
     * mybatis plus分页查询
     *
     * @param clazz   表实体class
     * @param page    页数
     * @param size    数据条数
     * @param wrapper 条件构造器，可为空
     * @param <R>     表实体类型
     * @return 查询完的分页对象
     */
    private <R> IPage<R> page(Class<R> clazz, Integer page, Integer size, Wrapper<R> wrapper) {
        IPage<R> mpPageObj
                = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        HashMap<String, Object> queryParamsMap = Maps.newHashMapWithExpectedSize(2);
        queryParamsMap.put(Constants.WRAPPER, wrapper);
        queryParamsMap.put(com.ncov.module.common.Constants.PAGE_PARAM_NAME, mpPageObj);
        return executeAndThen(clazz
                , sqlSession -> sqlSession.selectList(sqlStatement(clazz, SqlMethod.SELECT_PAGE)
                        , queryParamsMap)
                , mpPageObj::setRecords);
    }

    /**
     * 根据给定实体类型和条件构造器查询一条记录
     *
     * @param cls     表实体class
     * @param wrapper 条件构造器
     * @param <R>     表实体类型
     * @return 单条记录
     */
    public <R> R selectOne(Class<R> cls, Wrapper<R> wrapper) {
        List<R> list = selectList(cls, wrapper);
        return null != list && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 获取给定实体和sqlMethod的SqlStatement
     *
     * @param cls       class对象
     * @param sqlMethod mybatis plus提供的sql操作方法
     */
    protected String sqlStatement(Class<?> cls, SqlMethod sqlMethod) {
        return SqlHelper.table(cls).getSqlStatement(sqlMethod.getMethod());
    }

    private <S> S execute(Class<?> cls, Function<SqlSession, S> function) {
        Objects.requireNonNull(function, "function must be not null!");
        SqlSession sqlSession = SqlHelper.sqlSession(cls);
        try {
            return function.apply(sqlSession);
        } finally {
            SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(cls));
        }
    }

    private <S, R> S executeAndThen(Class<?> cls, Function<SqlSession, R> function, Function<R, S> after) {
        Objects.requireNonNull(function, "function must be not null!");
        Objects.requireNonNull(after, "after must be not null!");
        return execute(cls, function.andThen(after));
    }

}
