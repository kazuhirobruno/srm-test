import { useState, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { recebivelService } from '../services/recebivel-service';
import { PageResponse, RecebivelResponseDTO, ExtratoFiltros } from '../types/recebivel';

export function useExtrato() {
  const router = useRouter();
  const searchParams = useSearchParams();

  const [dados, setDados] = useState<PageResponse<RecebivelResponseDTO> | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  const filtros: ExtratoFiltros = {
    page: parseInt(searchParams.get('page') || '0', 10),
    size: parseInt(searchParams.get('size') || '10', 10),
    cedente: searchParams.get('cedente') || undefined,
    moeda: searchParams.get('moeda') || undefined,
    dataInicio: searchParams.get('dataInicio') || undefined,
    dataFim: searchParams.get('dataFim') || undefined,
  };

  const atualizarFiltros = (novosFiltros: Partial<ExtratoFiltros>) => {
    const params = new URLSearchParams(searchParams.toString());
    
    Object.entries(novosFiltros).forEach(([chave, valor]) => {
      if (valor !== undefined && valor !== '') {
        params.set(chave, valor.toString());
      } else {
        params.delete(chave);
      }
    });

    if (!novosFiltros.hasOwnProperty('page')) {
      params.set('page', '0');
    }

    router.push(`?${params.toString()}`);
  };

  useEffect(() => {
    async function carregarDados() {
      try {
        setCarregando(true);
        setErro(null);
        const resposta = await recebivelService.obterExtrato(filtros);
        setDados(resposta);
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      } catch (err: any) {
        setErro(err.message || 'Erro ao carregar o extrato do motor.');
      } finally {
        setCarregando(false);
      }
    }

    carregarDados();
  }, [searchParams]);

  return { dados, carregando, erro, filtros, atualizarFiltros };
}
