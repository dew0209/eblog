<#macro paging pageData>
    <div style="text-align: center">
            <div id="laypage-main">

            </div>
            <script>
                layui.use('laypage',function () {
                    var laypage = layui.laypage;
                    laypage.render({
                        elem:'laypage-main',
                        count:${pageData.total},
                        curr:${pageData.current},
                        limit:${pageData.size},
                        /* 回调函数 */
                        jump:function (obj,first) {
                            if(!first){
                                location.href = "?pn=" + obj.curr;
                            }
                        }
                    });
                })
            </script>
        </div>
</#macro>
<#macro postlist post>
    <li>
        <a href="/user/${post.authorId}" class="fly-avatar">
            <img src="${post.authorAvatar}" alt="${post.authorName}">
        </a>
        <h2>
            <a class="layui-badge">${post.categoryName}</a>
            <a href="/post/${post.id}">${post.title}</a>
        </h2>
        <div class="fly-list-info">
            <a href="/user/${post.authorId}" link>
                <cite>${post.authorName}</cite>
                <!--
                <i class="iconfont icon-renzheng" title="认证信息：XXX"></i>
                <i class="layui-badge fly-badge-vip">VIP3</i>
                -->
            </a>
            <span>${timeAgo(post.created)}</span>
            <span class="fly-list-nums">
                <i class="iconfont icon-pinglun1" title="回答"></i> ${post.commentCount}
              </span>
        </div>
        <div class="fly-list-badge">
            <#if post.level gt 0><span class="layui-badge layui-bg-black">置顶</span></#if>
            <#if post.recommend><span class="layui-badge layui-bg-red">精帖</span></#if>
        </div>
    </li>
</#macro>

<#macro centerLeft level>
<ul class="layui-nav layui-nav-tree layui-inline" lay-filter="user">
    <li class="layui-nav-item  <#if level == 0>layui-this</#if>">
        <a href="/user/home">
            <i class="layui-icon">&#xe609;</i>
            我的主页
        </a>
    </li>
    <li class="layui-nav-item  <#if level == 1>layui-this</#if>">
        <a href="/user/index">
            <i class="layui-icon">&#xe612;</i>
            用户中心
        </a>
    </li>
    <li class="layui-nav-item <#if level == 2>layui-this</#if>">
        <a href="/user/set">
            <i class="layui-icon">&#xe620;</i>
            基本设置
        </a>
    </li>
    <li class="layui-nav-item  <#if level == 3>layui-this</#if>">
        <a href="/user/mess">
            <i class="layui-icon">&#xe611;</i>
            我的消息
        </a>
    </li>
</ul>
</#macro>