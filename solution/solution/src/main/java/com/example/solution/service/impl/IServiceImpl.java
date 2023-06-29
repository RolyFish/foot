package com.example.solution.service.impl;

import com.example.solution.common.config.ComponentBean;
import com.example.solution.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IServiceImpl implements IService {

    private final ComponentBean componentBean;

    @Override
    public void service() {
        componentBean.doWrite();
    }

}
