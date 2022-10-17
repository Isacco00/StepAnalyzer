package stepanalyzer.manager.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import stepanalyzer.bean.CurrencyBean;
import stepanalyzer.manager.CurrencyManager;
import stepanalyzer.mapper.CurrencyMapper;
import stepanalyzer.repository.CurrencyRepository;
import stepanalyzer.request.bean.CurrencyRequestBean;

@Service
@Transactional
public class CurrencyManagerImpl implements CurrencyManager {

	@Inject
	private CurrencyRepository repo;
	@Inject
	private CurrencyMapper mapper;

	@Override
	public List<CurrencyBean> getCurrencyList(CurrencyRequestBean request) {
		return mapper.mapEntitiesToBeans(repo.getCurrencyList(request));
	}

}
