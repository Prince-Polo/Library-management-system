import React from 'react'
import { useHistory } from 'react-router'
import myImage from './background.png'
import { Button, Card, Input, Select, Pagination } from 'antd';
import './app.css';

export function Library(){
    const history=useHistory()
    return (
        <div style={{ background: '#f0f0aa', padding: '20px' }} className="library">
            {/* 顶部导航栏 */}
            <nav>
                <ul>
                    <li><a href="#">首页</a></li>
                    <li><a href="#">咨询</a></li>
                    <li><a href="#">服务办理</a></li>
                    <li><a href="#">我的图书馆</a></li>
                </ul>
            </nav>

            {/* 页面主体 */}
            <section className="content">
                <h1 style={{ color: '#477', fontSize: '24px' }}>Banteng's Library</h1>


                {/* 搜索框 */}
                <form action="/search" method="get">
                    <input type="text" placeholder="请输入关键字进行检索" />
                    <button type="submit">搜索</button>
                </form>

                {/* 功能按钮 */}
                <div className="function-buttons">
                    <button>登录</button>
                    <button>注册</button>
                    <button onClick={() => history.push('/')}>返回</button>
                </div>

                {/* 图书列表 */}
                <table>
                    <tr style={{color:'yellow'}}>
                        <th>序号</th>
                        <th>书名</th>
                        <th>作者</th>
                        <th>出版社</th>
                        <th>状态</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Type-safe Modern Systems</td>
                        <td>Banteng Aurochs</td>
                        <td>Java Banteng Press</td>
                        <td>在借</td>
                    </tr>
                </table>

                {/*{/* 分页 *\/
                <Pagination totalCount={100} currentPage={1} onChange={pageChange}>
                    <span>共100页</span>
                </Pagination>*/}

                {/* 广告 */}
                <div className="advertisement">
                    <img src={myImage} alt="广告图片" width="80%"
                         height="80%" />
                </div>

                {/* 通知公告 */}
                <div className="notice">
                    <h2>通知公告</h2>
                    <ul>
                        <li>[2023-05-06] 图书馆将于5月7日闭馆</li>
                        <li>[2023-04-28] 关于举办“阅读春天”活动的通知</li>
                    </ul>
                </div>

                {/* 底部链接 */}
                <footer>
                    <ul>
                        <li><a href="#">关于我们</a></li>
                        <li><a href="#">联系我们</a></li>
                        <li><a href="#">隐私政策</a></li>
                        <li><a href="#">站点地图</a></li>
                    </ul>
                </footer>
            </section>
        </div>
    );
}

