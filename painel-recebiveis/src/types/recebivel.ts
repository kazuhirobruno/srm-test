export enum TipoRecebivel {
  DUPLICATA = 'DUPLICATA',
  CHEQUE = 'CHEQUE'
}

export interface RecebivelCadastroDTO {
  valorOriginal: number;
  vencimento: string;
  tipo: TipoRecebivel;
  cedente: string;
  moedaOriginal: string;
}

export interface SimulacaoResultadoDTO {
  valorOriginal: number;
  valorLiquido: number;
  taxaAplicada: number;
  spreadCobrado: number;
  moedaLiquidacao: string;
}

export interface RecebivelResponseDTO {
  id: number;
  valorOriginal: number;
  valorLiquidado: number;
  vencimento: string;
  tipo: TipoRecebivel;
  status: 'PENDENTE' | 'LIQUIDADO';
  cedente: string;
  moedaOriginal: string;
  moedaLiquidacao: string;
  dataCriacao: string;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface ExtratoFiltros {
  page?: number;
  size?: number;
  dataInicio?: string;
  dataFim?: string;
  cedente?: string;
  moeda?: string;
}

export interface TaxaCambioDTO {
  id?: number;
  moedaOrigem: string;
  moedaDestino: string;
  fatorConversao: number;
  dataAtualizacao?: string;
}