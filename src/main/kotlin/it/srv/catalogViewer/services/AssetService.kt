package it.srv.catalogViewer.services

import it.srv.catalogViewer.dao.AssetsDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AssetService {

    @Autowired
    lateinit var assetDAO: AssetsDAO


}