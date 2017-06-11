package s.im.service.route.api;

import s.im.entity.domian.ChatMessageDO;

/**
 * 路由聊天消息至客服
 *
 * Created by za-zhujun on 2017/5/30.
 */
public interface MessageRouteService {
    void route(ChatMessageDO chatMessageDO);
}
