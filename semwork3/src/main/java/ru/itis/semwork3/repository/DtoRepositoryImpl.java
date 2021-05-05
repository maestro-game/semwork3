package ru.itis.semwork3.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.semwork3.dto.contentsource.PreviewSourceDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DtoRepositoryImpl implements DtoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PreviewSourceDto> previewSourceDtoRowMapper = (rs, rowNum) -> new PreviewSourceDto(rs.getLong("id"),
            rs.getString("name"),
            rs.getString("lastMessageShortText"),
            rs.getTimestamp("lastMessageTimestamp"));

    @Override
    public List<PreviewSourceDto> findAllPreviewSourceDtoByMember(Long id) {
        return jdbcTemplate.query("select s.id as id, s.name as name, m.text as lastMessageShortText, m.created as lastMessageTimestamp " +
                "from content_source as s " +
                "         join user_source as u on (s.id = ? or u.user_id = ?) and u.source_id = s.id" +
                "         join (select distinct on (m.source_id) m.source_id, m.text, m.created from message as m order by m.source_id, m.created desc) as m on s.id = m.source_id;", previewSourceDtoRowMapper, id, id);
    }
}
