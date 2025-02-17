package com.spam.whidy.api.admin;

import com.spam.whidy.api.common.ExcelExporter;
import com.spam.whidy.api.common.ExcelMapper;
import com.spam.whidy.application.place.PlaceDataCollectService;
import com.spam.whidy.application.place.PlaceRequestService;
import com.spam.whidy.application.place.PlaceService;
import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.application.user.UserService;
import com.spam.whidy.common.config.auth.Auth;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.placeRequest.PlaceRequest;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.dto.place.PlaceRegisterDTO;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import com.spam.whidy.dto.user.GrantRoleRequest;
import com.spam.whidy.dto.user.UserSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "관리자 페이지")
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserFinder userFinder;
    private final UserService userService;
    private final PlaceService placeService;
    private final PlaceRequestService placeRequestService;
    private final PlaceDataCollectService placeDataCollectService;

    @GetMapping("/user/search")
    @Operation(summary = "사용자 목록 조회", description = "Super Admin 권한 필요")
    public List<User> searchUsers(@Auth LoginUser loginUser, UserSearchCondition condition){
        checkSuperAdmin(loginUser.getUserId());
        return userFinder.findByCondition(condition);
    }

    @PatchMapping("/user/role")
    @Operation(summary = "사용자 권한 부여", description = "Super Admin 권한 필요")
    public void grantPrivilegeToUser(@Auth LoginUser loginUser, @RequestBody @Valid GrantRoleRequest grantRoleRequest){
        checkSuperAdmin(loginUser.getUserId());
        userService.updateRole(grantRoleRequest.userId(), grantRoleRequest.role());
    }

    @GetMapping("/place-request/search")
    @Operation(summary = "요청된 장소 목록 조회", description = "사용자가 장소 등록을 요청한 목록을 조회. (Admin 권한 필요)")
    public List<PlaceRequest> searchPlaceRequest(@Auth LoginUser user, PlaceRequestSearchCondition condition){
        checkAdmin(user.getUserId());
        return placeRequestService.searchByCondition(condition);
    }

    @PostMapping("/place/register")
    @Operation(summary = "장소 추가 (논의필요)", description = "사용자가 요청한 장소를 등록. (Admin 권한 필요)")
    public void registerPlace(@Auth LoginUser loginUser){
        checkAdmin(loginUser.getUserId());
    }

    @PostMapping("/place/collect")
    @Operation(summary = "장소 크롤링 api (테스트용)", description = "테스트용 api")
    public void collect(@Auth LoginUser loginUser){
        checkAdmin(loginUser.getUserId());
        placeDataCollectService.collectAll();
    }

    @PostMapping("/place/upload")
    @Operation(summary = "장소 등록 엑셀 파일 업로드")
    public void placeDataUpload(@Auth LoginUser loginUser, @RequestParam MultipartFile file) throws Exception {
        checkAdmin(loginUser.getUserId());
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())){
            Sheet sheet = workbook.getSheetAt(0);
            List<PlaceRegisterDTO> places = ExcelMapper.mapExcelToObjects(sheet, PlaceRegisterDTO.class);
            callSave(places);
        }
    }

    @GetMapping("/place/excel-form/download")
    @Operation(summary = "장소 등록 엑셀 폼 다운로드")
    public ResponseEntity<byte[]> downloadExcel(@Auth LoginUser loginUser) throws Exception{
        checkAdmin(loginUser.getUserId());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ExcelExporter.exportToExcel(List.of(new PlaceRegisterDTO()), PlaceRegisterDTO.class, out);
            byte[] bytes = out.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=place-form.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return ResponseEntity.ok().headers(headers).body(bytes);
        }
    }

    private void checkSuperAdmin(Long userId){
        Optional<User> user = userFinder.findById(userId);
        if(user.isEmpty() || !user.get().getRole().isSuperAdmin()){
            throw new BadRequestException(ExceptionType.FORBIDDEN);
        }
    }

    private void checkAdmin(Long userId){
        Optional<User> user = userFinder.findById(userId);
        if(user.isEmpty() || !user.get().getRole().isAdmin()){
            throw new BadRequestException(ExceptionType.FORBIDDEN);
        }
    }

    private void callSave(List<PlaceRegisterDTO> places) {
        for(PlaceRegisterDTO dto : places){
            try {
                placeService.save(dto.toEntity());
            }catch (DataIntegrityViolationException e){
                // 제약 키로 인해 저장되지 않는 데이터는 무시한다.
            }
        }
    }
}
