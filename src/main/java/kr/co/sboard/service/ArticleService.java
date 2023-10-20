package kr.co.sboard.service;

import kr.co.sboard.dto.ArticleDTO;
import kr.co.sboard.dto.FileDTO;
import kr.co.sboard.dto.PageRequestDTO;
import kr.co.sboard.dto.PageResponseDTO;
import kr.co.sboard.entity.ArticleEntity;
import kr.co.sboard.repository.ArticleRepository;
import kr.co.sboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.PageRanges;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FileRepository fileRepository;
    //maven 에서 다운받아 쓰는것 entity를 dto로 dto를 entity로 변환시켜줌 been 설정도 해야함
    private final ModelMapper modelMapper;

    public PageResponseDTO findByParentAndCate(PageRequestDTO pageRequestDTO){

        //sort 정렬값 들어가야함 그래서 no
        Pageable pageable = pageRequestDTO.getPageable("no");

        //
        //Pageable pageable = PageRequest.of(pg-1,10, Sort.Direction.DESC,"no");
        Page<ArticleEntity> result = articleRepository.findByParentAndCate(0, pageRequestDTO.getCate(), pageable);

        // dtoList(게시물 10개있음) 안에있는 리스트를 stream(통로)이용해서 map(자료구조에서 즉 리스트안에있는 값들을 동일한 연산처리를 수행함)처리
        List<ArticleDTO> dtoList = result.getContent()
                                        .stream()
                                        .map(entity -> modelMapper.map(entity, ArticleDTO.class))
                                        .toList();

        //전체 게시판 갯수
        int totalElement = (int) result.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(totalElement)
                .build();
    }


    public void save(ArticleDTO dto){

      ArticleEntity savedEntity = articleRepository.save(dto.toEntity());

      //파일 업로드
      FileDTO fileDTO = fileUpload(dto);

      if(fileDTO != null){
          // 파일 등록
          fileDTO.setAno(savedEntity.getNo());
          fileRepository.save(fileDTO.toEntity());
      }
    }

    @Value("${spring.servlet.multipart.location}")
    private String filePath;

    public FileDTO fileUpload(ArticleDTO dto) {

        MultipartFile mf = dto.getFname();

        if(!mf.isEmpty()){
            // 파일 첨부 했을 경우
            String path = new File(filePath).getAbsolutePath();


            String oName = mf.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf("."));
            String sName = UUID.randomUUID().toString()+ext;

            try {
                mf.transferTo(new File(path, sName));
            } catch (IOException e) {
               log.error(e.getMessage());
            }

            return FileDTO.builder().ofile(oName).sfile(sName).build();

        }
        return null;

    }



}
