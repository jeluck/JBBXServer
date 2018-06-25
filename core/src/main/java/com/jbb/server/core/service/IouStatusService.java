 package com.jbb.server.core.service;

import com.jbb.server.core.domain.Iou;

public interface IouStatusService {
     void updateStatus(Iou iou, int newStatus, int userId, String extentionDateTs, Integer lenderId);
}
