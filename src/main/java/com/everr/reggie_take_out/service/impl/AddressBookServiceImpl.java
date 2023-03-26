package com.everr.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.everr.reggie_take_out.entiy.AddressBook;
import com.everr.reggie_take_out.mapper.AddressBookMapper;
import com.everr.reggie_take_out.service.AddressBookService;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
