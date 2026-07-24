import { apiFetch } from './api';
import { 
  RecebivelCadastroDTO, 
  SimulacaoResultadoDTO, 
  RecebivelResponseDTO, 
  PageResponse, 
  ExtratoFiltros 
} from '@/types/recebivel';

export const recebivelService = {
  simular: async (dados: RecebivelCadastroDTO): Promise<SimulacaoResultadoDTO> => {
    return apiFetch<SimulacaoResultadoDTO>('/recebiveis/simular', {
      method: 'POST',
      body: JSON.stringify(dados),
    });
  },

  cadastrar: async (dados: RecebivelCadastroDTO): Promise<RecebivelResponseDTO> => {
    return apiFetch<RecebivelResponseDTO>('/recebiveis', {
      method: 'POST',
      body: JSON.stringify(dados),
    });
  },

  obterExtrato: async (filtros: ExtratoFiltros): Promise<PageResponse<RecebivelResponseDTO>> => {
    const params = new URLSearchParams();
    
    if (filtros.page !== undefined) params.append('page', filtros.page.toString());
    if (filtros.size !== undefined) params.append('size', filtros.size.toString());
    if (filtros.cedente) params.append('cedente', filtros.cedente);
    if (filtros.moeda) params.append('moeda', filtros.moeda);
    if (filtros.dataInicio) params.append('dataInicio', filtros.dataInicio);
    if (filtros.dataFim) params.append('dataFim', filtros.dataFim);

    return apiFetch<PageResponse<RecebivelResponseDTO>>(`/relatorios/extrato?${params.toString()}`, {
      method: 'GET',
      next: { revalidate: 0 }
    });
  }
};