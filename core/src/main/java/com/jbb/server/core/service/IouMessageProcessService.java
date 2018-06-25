package com.jbb.server.core.service;

import com.jbb.server.core.domain.Iou;

public interface IouMessageProcessService {

    void sendIouMessage(int preStatus, Iou iou);

    void sendIouAlertMessage(Iou iou, boolean isLenderMsg, int days);

}
