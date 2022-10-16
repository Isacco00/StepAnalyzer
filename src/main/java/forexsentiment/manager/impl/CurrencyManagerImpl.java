package forexsentiment.manager.impl;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import forexsentiment.bean.CurrencyBean;
import forexsentiment.manager.CurrencyManager;
import forexsentiment.mapper.CurrencyMapper;
import forexsentiment.repository.CurrencyRepository;
import forexsentiment.request.bean.CurrencyRequestBean;

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
